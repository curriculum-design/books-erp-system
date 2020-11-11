<template lang="pug">
  .phone-select-container
      el-row(:gutter="20")
          el-col(:span="4")
              el-menu.brand-list(:default-active="currentBrand" @select="v => currentBrand = v")
                  el-menu-item(v-for="b in brand", :key="b", :index="`${b}`") {{optionsClassify[b][0].key}}
          el-col(:span="20")
              .phone-list
                  LocalTable(:data="currentPrds" ref="table")
                      el-table-column(type="selection" width="55")
                      el-table-column(prop="valueId", label="选项ID")
                      el-table-column(prop="value", label="选项名称")
</template>

<script>
export default {
    data() {
        return {
            brand: [],
            prds: [],
            currentBrand: '',
            optionsClassify: {}
        }
    },
    computed: {
        tableComponent() {
            return this.$refs.table.tableComponent
        },
        currentPrds() {
            let brandId = parseInt(this.currentBrand)
            if (brandId === 0) {
                return this.prds
            }
            return this.prds.filter(p => p.keyId === brandId)
        },
    },
    async created() {
        let options = await this.$commonService.options()
        console.log('options', options)
        options.forEach(option => {
            this.optionsClassify[option.keyId] ? this.optionsClassify[option.keyId].push(option) : this.optionsClassify[option.keyId] = []
        })
        console.log(this.optionsClassify)
        this.brand = Object.keys(this.optionsClassify)
        // this.brand = await this.$api.bsbPrdCtg.all()
        this.currentBrand = this.brand[0] + ''
        this.prds = options
        this.tableComponent.$on('selection-change', this.selectChange)
    },
    methods: {
        reset() {
            this.tableComponent.clearSelection()
        },
        selectChange(e) {
            this.$emit('select', [...e])
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
}
</style>
