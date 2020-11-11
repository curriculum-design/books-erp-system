// import scriptjs from 'scriptjs'
// import dateformat from 'dateformat'

// const printPromise = new Promise((resolve, reject) => {
//     let lodjs = 'https://localhost:8443/CLodopfuncs.js?name=CLODOPA'
//     if (location.protocol === 'http:') {
//         lodjs = 'http://localhost:8000/CLodopfuncs.js?name=CLODOPALoc'
//     }
//     scriptjs([lodjs], () => {
//         try {
//             const LODOP = window.getCLodop()
//             LODOP.SET_LICENSES('', '0221D2C14AFC0A439E81707A93415AE9', 'C94CEE276DB2187AE6B65D56B3FC2848', '')
//             LODOP.SET_PRINTER_INDEX(-1)
//             LODOP.SET_PRINT_MODE('CATCH_PRINT_STATUS', true)
//             resolve(LODOP)
//         } catch (error) {
//             console.log('init loop field', error)
//         }
//     })
// })

export default {
    reversalObject(obj = {}) {
        const reversal = {}
        Object.keys(obj).forEach(k => {
            reversal[obj[k]] = k
        })
        return reversal
    },
    likeMatch(source, filterKey) {
        const isMatch = (s, k) => {
            const trimSource = (s + '').replace(/ /g, '').toLowerCase()
            const trimFilterKey = (k + '').replace(/ /g, '').toLowerCase()
            return trimSource.indexOf(trimFilterKey) > -1
        }
        if (source) {
            if (filterKey) {
                if (Array.isArray(source)) {
                    return source.filter(s => isMatch(s, filterKey)).length > 0
                }
                return isMatch(source, filterKey)
            }
            return true
        }
        return false
    },
    stringToCamels(string) {
        if (string) {
            string = string.trim().replace(/_(\w)/g, ($0, $1) => {
                return $1.toUpperCase()
            })
        }
        return string
    },
    sumArray(array, ...fields) {
        const result = fields.map(d => 0)
        if (array.length) {
            fields.forEach((f, index) => {
                const filterArray = array.map(item => item[f]).filter(v => v).map(v => Number.parseFloat(v))
                if (filterArray.length) {
                    let sum = filterArray.reduce((a, b) => a + b)
                    result[index] = sum
                }
            })
        }
        return result
    },
    transKeyValue(data) {
        let newData = {}
        Object.keys(data).forEach(k => {
            let value = data[k]
            newData[value] = k
        })
        return newData
    },
    arrayToMap(array, nameKey, valueKey) {
        let map = {}
        array.forEach(item => {
            map[item[nameKey]] = item[valueKey]
        })
        return map
    },
    arrayToArrayMap(array, namekay) {
        let map = {}
        array.forEach(item => {
            let key = item[namekay]
            let array = map[key] || []
            array.push(item)
            map[key] = array
        })
        return map
    },
    arrayToObjectMap(array, key, format = e => e) {
        let map = {}
        array.forEach(item => {
            map[item[key]] = format(item)
        })
        return map
    },
    arrayToTree(array, idKey, parentKey) {
        const configMap = {}
        const allConfig = array.map(config => {
            let m = {
                id: config[idKey],
                ...config,
                children: []
            }
            configMap[config[idKey]] = m
            return m
        })
        const root = []
        allConfig.forEach(m => {
            if (m[parentKey] && configMap[m[parentKey]]) {
                configMap[m[parentKey]].children.push(m)
            } else {
                root.push(m)
            }
        })
        return root
    },
    getValue(data, key) {
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
    },
    dataURItoBlob(dataURI) {
        const byteString = atob(dataURI.split(',')[1])
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]
        var ab = new ArrayBuffer(byteString.length)
        var ia = new Uint8Array(ab)
        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i)
        }
        return new Blob([ab], {type: mimeString})
    },
    objectToArray(data) {
        let newData = []
        Object.keys(data).forEach(k => {
            let value = data[k]
            newData.push({
                key: value,
                value: k
            })
        })
        return newData
    },
    digitUppercase(n) {
        const fraction = ['角', '分']
        const digit = [
            '零', '壹', '贰', '叁', '肆',
            '伍', '陆', '柒', '捌', '玖'
        ]
        const unit = [
            ['元', '万', '亿'],
            ['', '拾', '佰', '仟']
        ]
        const head = n < 0 ? '欠' : ''
        n = Math.abs(n)
        let s = ''
        for (let i = 0; i < fraction.length; i++) {
            s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '')
        }
        s = s || '整'
        n = Math.floor(n)
        for (let i = 0; i < unit[0].length && n > 0; i++) {
            let p = ''
            for (let j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[n % 10] + unit[1][j] + p
                n = Math.floor(n / 10)
            }
            s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s
        }
        return head + s.replace(/(零.)*零元/, '元')
            .replace(/(零.)+/g, '零')
            .replace(/^整$/, '零元整')
    }
}
