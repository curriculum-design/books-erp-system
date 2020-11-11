<template lang="pug">
el-table.mini-table(border, :data="priceTemplate" v-if="isInit")
    el-table-column(type="index", width="40")
    el-table-column(prop="detailname", label="选项名称", width="120")
    el-table-column(:label="isDialog ? '上次选项' : '顾客自选'", width="200")
        template(slot-scope="scope")
            span {{priceOptions[scope.row.detailid] && priceOptions[scope.row.detailid].seldetailnameO}}
    el-table-column(label="检测选项", width="240" v-if="!hiddenDetail")
        template(slot-scope="scope" v-if="scope.row.detailid")
            el-select(v-model="options[scope.row.detailid].childDetailid" placeholder="请选择", size="mini", @change="a => selectChangeHandler(a, options[scope.row.detailid])", multiple v-if="options[scope.row.detailid] && options[scope.row.detailid].isMultiple")
                el-option(v-for="item in scope.row.detailChildren" ,:key="item.detailid", :label="item.detailname", :value="item.detailid")
            el-select(v-model="options[scope.row.detailid].childDetailid[0]" placeholder="请选择", size="mini", @change="a => selectChangeHandler(a, options[scope.row.detailid])",  v-else)
                el-option(v-for="item in scope.row.detailChildren" ,:key="item.detailid", :label="item.detailname", :value="item.detailid")
    slot
    el-table-column(label="实际情况", width="240")
        template(slot-scope="scope" v-if="scope.row.detailid")
            el-select(v-model="options[scope.row.detailid].newDetailid" placeholder="请选择", size="mini",
            :class="options[scope.row.detailid].newDetailid !== options[scope.row.detailid].oldDetailid ? 'diff-red' : ''"
            :disabled="!hiddenDetail")
                el-option(v-for="item in scope.row.children" ,:key="item.detailid", :label="item.detailname", :value="item.detailid")

</template>

<script>
import CommonUtils from '@/utils/CommonUtils'
export default {
    props: ['detectionDetail', 'priceTemplate', 'value', 'readonly', 'isDialog', 'hiddenDetail'],
    data() {
        return {
            options: {},
            isInit: false,
        }
    },
    created() {
        this.initOptions()
    },
    watch: {
        priceTemplate(val) {
            this.initOptions()
        },
    },
    methods: {
        selectChangeHandler(val, option) {
            if (!option.isMultiple) {
                const detailItem = this.allDetailIdMap[val]
                if (detailItem) {
                    option.newDetailid = detailItem.pdetailid
                } else {
                    option.newDetailid = val
                }
            } else {
                if (val && val.length) {
                    const filterOptions = this.allDetailsMap[option.pdetailid]
                        .filter(a => val.indexOf(a.detailid) > -1)
                    const details = filterOptions.sort((a, b) => b.orderid - a.orderid)[0]
                    option.newDetailid = details.parentDetailid
                } else {
                    option.newDetailid = null
                }
            }
            this.$emit('input', {...this.options})
        },
        initOptions() {
            let priceTemplate = this.priceTemplate
            let options = {}
            priceTemplate.forEach(val => {
                let oldOption = this.priceOptions[val.detailid] || {}
                let newDetailId = oldOption.seldetailidC
                let childDetailid = []
                if (childDetailid.length === 0) {
                    if (oldOption.detectionDetailItems && oldOption.detectionDetailItems.length) {
                        childDetailid = oldOption.detectionDetailItems.map(d => d.newDetailid)
                    } else {
                        let defaultChildId
                        const newDetailOption = this.allPriceItemMap[newDetailId] || this.allPriceItemMap[oldOption.seldetailidO]
                        if (newDetailOption) {
                            if (this.allDetailIdMap[newDetailOption.seldetailid]) {
                                defaultChildId = newDetailOption.seldetailid
                                childDetailid.push(defaultChildId)
                            } else {
                                childDetailid.push(newDetailOption.detailid)
                            }
                        }
                    }
                }
                if (!this.judgeExistInPriceTemplate(val.detailid, oldOption.seldetailidC)) {
                    newDetailId = oldOption.seldetailidO
                }
                options[val.detailid] = {
                    isMultiple: val.isMultiple,
                    pdetailid: val.detailid,
                    oldDetailid: oldOption.seldetailidO,
                    newDetailid: newDetailId,
                    childDetailid: childDetailid,
                    remark: oldOption.remark,
                    remarkArray: oldOption.remark ? oldOption.remark.toString().split(',') : []
                }
            })
            this.options = options
            this.$emit('input', {...options})
            this.isInit = true
        },
        judgeExistInPriceTemplate(detailid, option) {
            let parent = this.detectionDetail.priceTemplate.filter((item) => parseInt(item.detailid) === parseInt(detailid))
            return parent[0].children.filter((child) => parseInt(child.detailid) === parseInt(option)).length
        }
    },
    computed: {
        allDetailsMap() { // 细项数据
            let detailsMap = {}
            this.detectionDetail.bsvDetails.forEach(detailItem => {
                const {optionDetailid} = detailItem
                const array = detailsMap[optionDetailid] || []
                array.push(detailItem)
                detailsMap[optionDetailid] = array
            })
            return detailsMap
        },
        allDetailIdMap() {
            return CommonUtils.arrayToObjectMap(this.detectionDetail.bsvDetails, 'detailid')
        },
        allPriceItemMap() {
            const allPriceItemMap = {}
            this.priceTemplate.forEach(a => {
                a.children && a.children.forEach(item => {
                    allPriceItemMap[item.detailid] = item
                })
            })
            return allPriceItemMap
        },
        priceOptions() {
            let temObj = {}
            this.detectionDetail.priceOptions.forEach(val => {
                if (!temObj[val.detailid]) {
                    temObj[val.detailid] = val
                }
            })
            return temObj
        },
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.diff-red {
    .el-input__inner {
        background-color:@danger-color !important;
        color: #fff !important;
    }
}
</style>
