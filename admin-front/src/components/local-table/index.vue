<template lang="pug">
  .local-page-table
      el-input.margin-bottom-10(:placeholder="placeholder || '全表搜索'" v-model="filter" v-if="search")
      el-table.margin-bottom-10(:show-summary="showSummary", :data="currentData" ref="table", :height="height", :border="border", :summary-method="summaryMethod", @selection-change="a => $emit('selection-change', a)" :highlightCurrentRow="highlightCurrentRow", @row-click="rowClickHandler")
        slot
      .flex-between
        el-pagination(:pageNum.sync="pageNum", :total="total", :pageSize="pageSize",
        :page-sizes="[10, 20, 30, 40, 50, 9999]",
        @size-change="sizeChangeHandler" layout="total, sizes, prev, pager, next", v-if="pagination")
        slot(name="after-page")
</template>

<script>
import TableMix from '@/utils/mixins/table-mix'
export default {
    mixins: [TableMix],
    props: {
        border: {},
        showSummary: {},
        highlightCurrentRow: {type: Boolean, default: false},
        summaryMethod: {type: Function},
        placeholder: {},
        height: {},
        headerMapping: {
            type: Object,
            default: () => {}
        },
        data: {
            type: Array,
            default: () => []
        },
        search: {
            default: true
        },
        pagination: {
            default: true
        },
        pageSize: {
            default: 10,
        }
    },
    data() {
        return {
            pageNum: 1,
            filter: '',
            privatePageSize: '',
        }
    },
    created() {
        this.privatePageSize = this.pageSize
    },
    watch: {
        pageSize(size) {
            this.privatePageSize = size
        },
        data() {
            this.filter = ''
        }
    },
    computed: {
        tableComponent() {
            return this.$refs.table
        },
        filterData() {
            const mappings = Object.keys(this.headerMapping || {})
            return this.data.filter(row => {
                let values
                if (mappings.length) {
                    values = mappings.map(key => {
                        const valueDefine = this.headerMapping[key]
                        console.log(valueDefine, key, row)
                        return this.fieldParseByKey(valueDefine, key, row)
                    })
                } else {
                    values = Object.values(row)
                }
                return this.likeMatch(values, this.filter)
            })
        },
        total() {
            return this.filterData.length
        },
        currentData() {
            if (this.pagination) {
                let start = (this.pageNum - 1) * this.privatePageSize
                return this.filterData.slice(start, start + this.privatePageSize)
            }
            return this.filterData
        }
    },
    methods: {
        rowClickHandler(row, event, column) {
            console.log('row cliek')
            this.$emit('row-click', row, event, column)
        },
        sizeChangeHandler(val) {
            this.$emit('update:page-size', val)
            this.privatePageSize = val
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
