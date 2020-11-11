import XLSX from 'xlsx'
import FileSaver from 'file-saver'

function s2ab(s) {
    if (typeof ArrayBuffer !== 'undefined') {
        let buf = new ArrayBuffer(s.length)
        let view = new Uint8Array(buf)
        for (let i = 0; i !== s.length; ++i) {
            view[i] = s.charCodeAt(i) & 0xFF
        }
        return buf
    } else {
        let buf = new Array(s.length)
        for (let i = 0; i !== s.length; ++i) {
            buf[i] = s.charCodeAt(i) & 0xFF
        }
        return buf
    }
}

function processParse(parse, value, js) {
    let label = parse
    let format = e => e
    let dataMapping = {}
    if (typeof parse === 'object') {
        label = parse.label || label
        format = parse.format || format
        dataMapping = parse.dataMapping || {}
        let dataMappingRev = parse.dataMappingRev
        let parseMethod = parse.parseMethod
        if (dataMappingRev) {
            Object.keys(dataMappingRev).forEach(k => {
                dataMapping[dataMappingRev[k]] = k
            })
        }
        if (parseMethod) {
            value = parseMethod(js)
        }
    }
    value = dataMapping[value] || value
    value = format(value, js)
    return {label, value}
}

function jsonShellToBook(jsonData, opts) {
    let jsonSheel = XLSX.utils.json_to_sheet(getParseData(jsonData, opts))
    const Sheets = {Sheet1: jsonSheel}
    let book = {
        SheetNames: Object.keys(Sheets),
        Sheets
    }
    return book
}

function fixdata(data) {
    let o = ''
    let l = 0
    let w = 10240
    for (; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)))
    o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)))
    return o
}

function getMappingsWithoutFunction(headerMapping) {
    const mapping = {...headerMapping}
    const mappings = []
    for (let map in mapping) {
        let mapObject = mapping[map]
        if (typeof mapObject === 'string') {
            mapObject = {
                label: mapObject
            }
        } else {
            mapObject = {...mapObject}
            Object.keys(mapObject).forEach((k) => {
                if (typeof mapObject[k] === 'function') {
                    delete mapObject[k]
                }
            })
        }
        mappings.push(mapObject)
    }
    return mappings
}

function getValue(data, key) {
    if (key && typeof key.split === 'function') {
        const keyArray = key.split('.')
        let value
        let tempData = data
        keyArray.forEach((k) => {
            if (tempData) {
                value = tempData[k]
                tempData = value
            }
        })
        return tempData
    }
    return undefined
}

function getParseData(jsonData, {headerMapping = {}, onlyMapping = true} = {}) {
    const parseData = jsonData.map(js => {
        let data = {}
        let targetObject = js
        if (onlyMapping) {
            targetObject = headerMapping
        }
        const nameMap = {}
        Object.keys(targetObject).forEach((k, index) => {
            let parse = headerMapping[k] || k
            let rawValue = js[k]
            if (onlyMapping) {
                rawValue = getValue(js, k)
            }
            let {label, value} = processParse(parse, rawValue, js)
            if (data[label]) {
                nameMap[label] = (nameMap[label] || 0) + 1
                label = label + `(${nameMap[label]})`
            }
            data[label] = value
        })
        return data
    })
    return parseData
}

function exportHook(data, options) {
    const windowExportHook = window.$exportHook
    if (windowExportHook && typeof windowExportHook === 'function') {
        try {
            windowExportHook(data, options)
        } catch (error) {
            console.error('export hook error: ', error)
        }
    }
}

export default {
    processParse,
    getParseData,
    /**
     * 导出
     * @param  {[Array<Object>]} jsonData  [数组对象数据]
     * @param  {Object} [opts={}] [导出选项]
     * @param [opts.filename] export filename
     * @param [opts.headerMapping] json[key] to name mapping
     * @param [opts.onlyMapping] export headermapping includes fields
     * @return {[type]}           [description]
     */
    jsonExport(jsonData, opts = {}) {
        let {filename = `export_${Date.now()}`} = opts
        let sheet = jsonShellToBook(jsonData, opts)
        let type = 'xlsx'
        console.log('sheet ', sheet)
        let sheetOut = XLSX.write(sheet, {bookType: type, bookSST: true, type: 'binary'})
        filename = `${filename}.${type}`
        try {
            FileSaver.saveAs(
                new Blob([s2ab(sheetOut)], {type: 'application/octet-stream'}),
                filename)
            exportHook(jsonData, opts)
        } catch (e) {
            console.error(e)
            throw e
        }
    },
    jsonExportPromise(jsonData, opts, filename = opts.filename) {
        const promise = new Promise((resolve, reject) => {
            const ExportWorker = require('worker-loader!./export-worker')
            const worker = new ExportWorker()
            let parseData = getParseData(jsonData, opts)
            if (!filename) {
                filename = `export_${Date.now()}`
            }
            let {additRow} = opts
            const mappings = getMappingsWithoutFunction(opts.headerMapping)
            const fileType = 'xlsx'
            filename = `${filename}.${fileType}`
            worker.postMessage({parseData, filename, mappings, additRow})
            worker.onmessage = ({data: {success, blob, error}}) => {
                if (success) {
                    FileSaver.saveAs(blob, filename)
                    resolve()
                } else {
                    reject(error)
                }
            }
        })
        exportHook(jsonData, opts)
        return promise
    },
    importFile(file, {headerMapping} = {}) {
        return new Promise(function(resolve, reject) {
            const reader = new FileReader()
            reader.onload = e => {
                const data = e.target.result
                let array = btoa(fixdata(data))
                try {
                    let book = XLSX.read(array)
                    let sheetName = book.SheetNames[0]
                    let sheet = book.Sheets[sheetName]
                    const jsonData = XLSX.utils.sheet_to_json(sheet)
                    const parseData = jsonData.map(item => {
                        let parseData = {}
                        Object.keys(item).forEach(k => {
                            let value = item[k]
                            let cell = headerMapping[k]
                            let key = cell
                            if (typeof cell === 'object') {
                                key = cell.name
                                if (typeof cell.parse === 'function') {
                                    value = cell.parse(value.trim())
                                }
                            }
                            parseData[key] = value
                        })
                        delete parseData.undefined
                        return parseData
                    })
                    resolve({
                        parseData,
                        raw: jsonData
                    })
                } catch (e) {
                    console.error(e)
                    reject(e)
                }
            }
            reader.readAsArrayBuffer(file)
        })
    },
    exportExcelFromTable(dom) {
        let wb = XLSX.utils.table_to_book(dom)
        let wbout = XLSX.write(wb, {
            bookType: 'xlsx',
            compression: true,
            type: 'binary'
        })
        let filename = `export_${Date.now()}`
        try {
            FileSaver.saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'})
                , filename + '.xlsx')
        } catch (e) {
            console.log(e, wbout)
        }
        return wbout
    }
}
