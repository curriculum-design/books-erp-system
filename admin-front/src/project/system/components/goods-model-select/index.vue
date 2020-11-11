<template lang="pug">
  .phone-select-container
      el-row(:gutter="20")
          el-col(:span="4")
              el-menu.brand-list(:default-active="currentBrand" @select="v => currentBrand = v")
                  el-menu-item(v-for="b in brand", :key="b.id", :index="`${b.id}`") {{b.brandName}}
          el-col(:span="20")
              .phone-list
                  LocalTable(:data="currentPrds" ref="table")
                      el-table-column(type="selection" width="55")
                      el-table-column(prop="id", label="机型ID")
                      el-table-column(prop="modelName", label="机型名称")
</template>

<script>
export default {
    data() {
        return {
            brand: [],
            prds: [],
            currentBrand: ''
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
            return this.prds.filter(p => p.brandId === brandId)
        },
    },
    async created() {
        this.brand = await this.$api.shopGoodsBrand.all()
        this.currentBrand = this.brand[0].id + ''
        this.prds = await this.$api.shopGoodsModel.all()
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
