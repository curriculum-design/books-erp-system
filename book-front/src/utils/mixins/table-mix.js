import ExcelUtils from '@/utils/ExcelUtils'
import CommonUtils from '@/utils/CommonUtils'
function fieldParse(mapping, value, data) {
    let {
        value: returnValue
    } = ExcelUtils.processParse(mapping, value, data)
    return returnValue
}
function fieldParseByKey(mapping, key, data) {
    const keyArray = key.split('.')
    let value
    let tempData = data
    keyArray.forEach((k) => {
        value = tempData[k]
        tempData = value || {}
    })
    return fieldParse(mapping, value, data)
}
export default {
    components: {
        ColumnContent: {
            props: {
                columnDefine: '',
                row: '',
                valueKey: String,
                renderContent: Function,
            },
            render(h) {
                const {
                    columnDefine,
                    renderContent,
                    row,
                    valueKey,
                } = this
                const returnValue = fieldParseByKey(columnDefine, valueKey, row)
                return renderContent ? renderContent.call(this._renderProxy, h, {
                    row,
                    data: returnValue
                }) : (<span> {returnValue} </span>)
            }
        }
    },
    methods: {
        fieldParse,
        fieldParseByKey,
        likeMatch: CommonUtils.likeMatch,
        objectSpanMethod({ row, column, rowIndex, columnIndex }) {
            let option = this.headerMapping[Object.keys(this.headerMapping)[columnIndex]]
            if (columnIndex >= 0 && typeof option === 'object' && option['rowspan'] > 0) {
                if (rowIndex % option['rowspan'] === 0) {
                    return {
                        rowspan: option['rowspan'],
                        colspan: 1
                    }
                } else {
                    return {
                        rowspan: 0,
                        colspan: 0
                    }
                }
            }
            if (rowIndex >= 0) {
                if (columnIndex >= 0 && typeof option === 'object' && option['colspan'] > 0) {
                    return {
                        rowspan: 1,
                        colspan: option['colspan']
                    }
                } else if (columnIndex === 1) {
                    return [1, 1]
                }
            }
        },
        summaryFromHeaderMapping({columns, data}) {
            let headerMapping = this.headerMapping
            if (typeof this.getHeaderMapping === 'function') {
                headerMapping = this.getHeaderMapping()
            }
            let tableData = this.tableData
            if (typeof this.getTableData === 'function') {
                tableData = this.getTableData()
            }
            const summaryLocation = {}
            const summaryArray = []
            columns.forEach((c, index) => {
                const fieldName = c.property
                if (fieldName) {
                    const define = headerMapping[fieldName]
                    if (define.sum) {
                        summaryLocation[fieldName] = index
                    }
                }
            })

            // 填充0
            const summaryKeys = Object.keys(summaryLocation)
            if (summaryKeys.length) {
                summaryArray.push('合计')
                summaryKeys.forEach(key => {
                    summaryArray[summaryLocation[key]] = 0
                })
            } else {
                return []
            }

            // 汇总合计
            tableData.forEach(columnData => {
                summaryKeys.forEach(key => {
                    const location = summaryLocation[key]
                    summaryArray[location] += CommonUtils.getValue(columnData, key) || 0
                })
            })
            summaryArray.forEach((sum, index) => {
                if (sum && sum.toFixed && !Number.isInteger(sum)) {
                    summaryArray[index] = sum.toFixed(2)
                }
            })
            return summaryArray
        }
    }
}
