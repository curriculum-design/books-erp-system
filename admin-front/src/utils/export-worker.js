import XLSX from 'xlsx'
// import FileSaver from 'file-saver'
const fileType = 'xlsx'
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

function getSheelMateData(mappings, parseData) {
    const dataLength = parseData.length
    const colInfos = []
    const colspans = []
    const rowspans = []
    mappings.forEach((map, index) => {
        const {width, colspan, rowspan} = map
        if (width) {
            colInfos[index] = {width: width / 10 * 1.3}
        }
        if (rowspan) {
            for (let i = 1; i <= dataLength; i = i + rowspan) {
                rowspans.push({
                    s: {
                        c: index,
                        r: i
                    },
                    e: {
                        c: index,
                        r: i + rowspan - 1
                    }
                })
            }
        }
        if (colspan) {
            colspans.push({
                s: {
                    c: index,
                    r: 0,
                },
                e: {
                    c: index + colspan - 1,
                    r: 0,
                }
            })
        }
    })
    return {colInfos, colspans, rowspans}
}

/* eslint-disable no-undef */
onmessage = ({data: {parseData, mappings, additRow = []}}) => {
    let jsonSheel = XLSX.utils.json_to_sheet(parseData)
    if (additRow.length) {
        let currentRows = parseData.length + 2
        for (let i = 0; i < additRow.length; i++) {
            const row = additRow[i]
            row.forEach((data, index) => {
                const lastColumn = String.fromCharCode('A'.charCodeAt() + index) + (currentRows + i)
                jsonSheel[lastColumn] = {
                    t: typeof data === 'number' ? 'n' : 's',
                    v: data,
                }
            })
        }
        const ref = jsonSheel['!ref'].split(':')
        ref[1] = ref[1].replace(/[0-9]/ig, '') + (currentRows + additRow.length)
        jsonSheel['!ref'] = ref.join(':')
    }
    const {colInfos, colspans, rowspans} = getSheelMateData(mappings, parseData)
    jsonSheel['!merges'] = [...colspans, ...rowspans]
    jsonSheel['!cols'] = colInfos
    const Sheets = {Sheet1: jsonSheel}
    let sheet = {
        SheetNames: Object.keys(Sheets),
        Sheets
    }
    console.log('sheet ', sheet)
    let sheetOut = XLSX.write(sheet, {bookType: fileType, compression: true, type: 'binary'})
    try {
        postMessage({
            success: true,
            blob: new Blob([s2ab(sheetOut)], {type: 'application/octet-stream'})
        })
    } catch (e) {
        postMessage({
            success: false,
            error: e
        })
    }
}
