<template lang="pug">
  .phone-select-container
      el-row(:gutter="20")
          el-col(:span="4")
              el-menu.brand-list(:default-active="currentBrand" @select="v => currentBrand = v")
                  el-menu-item(v-for="b in brand", :key="b.ctgid", :index="`${b.ctgid}`") {{b.ctgname}}
          el-col(:span="20")
              .selected-list
                  el-tag(v-for="tag in selectedProp", :key="tag.prdid", @close="unselected(tag)" closable) {{tag.prdname}}
              .phone-list
                  LocalTable(:data="currentPrds" ref="table" height="500")
                      el-table-column(label="选择")
                          template(slot-scope="scope")
                              el-button(type="danger", @click="unselected(scope.row)", v-if="isSelect(scope.row)") 取消
                              el-button(type="primary", @click="selected(scope.row)", v-else) 选择
                      <!--el-table-column(type="selection" width="55")-->
                      el-table-column(prop="prdid", label="机型ID")
                      el-table-column(prop="prdname", label="机型名称")
</template>

<script>
export default {
    props: ['selectedData'],
    data() {
        return {
            brand: [],
            prds: [],
            currentBrand: '',
            selectedProp: []
        }
    },
    computed: {
        tableComponent() {
            return this.$refs.table.tableComponent
        },
        currentPrds() {
            let brandId = parseInt(this.currentBrand)
            if (brandId === 1) {
                return this.prds
            }
            return this.prds.filter(p => p.ctgid === brandId)
        },
        inputVlue() {
            // return this.oldData.toString().replace(',', '-')
        }
    },
    async created() {
        this.brand = await this.$api.bsbPrdCtg.all()
        this.currentBrand = this.brand[0].ctgid + ''
        this.prds = this.$store.getters.allProduct
        this.tableComponent.$on('selection-change', this.selectChange)
        window.setTimeout(() => {
            this.selectData()
        }, 200)
    },
    methods: {
        isSelect(item) {
            if (this.selectedData && this.selectedData.length > 0) {
                return this.selectedData.filter(prdid => {
                    return item.prdid === parseInt(prdid)
                }).length > 0
            }
        },
        selected(row) {
            this.selectedData.push(row.prdid)
            this.selectData()
        },
        unselected(row) {
            this.selectedData = this.selectedData.filter(prdid => {
                return row.prdid !== parseInt(prdid)
            })
            this.selectData()
        },
        selectData() {
            if (this.selectedData && this.selectedData.length > 0) {
                let data = []
                this.prds.map(item => {
                    this.selectedData.map(prdid => {
                        if (parseInt(prdid) === item.prdid) {
                            data.push(item)
                        }
                    })
                })
                this.selectedProp = data
                this.selectChange(data)
            } else {
                this.selectedData = []
                this.selectedProp = []
                this.selectChange([])
            }
        },
        reset() {
            this.tableComponent.clearSelection()
        },
        selectChange(e) {
            if (e.length) {
                this.$emit('select', [...e])
            } else {
                this.$emit('select', [])
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.phone-select-container{
    .brand-list{
        max-height: 600px;
        overflow: auto;
    }
    .selected-list {
        border: 1px solid #dcdfe6;
        border-radius: 5px;
        padding: 10px;
        margin-bottom: 10px;
        .el-tag--mini {
            margin: 5px;
        }
    }
}
</style>
