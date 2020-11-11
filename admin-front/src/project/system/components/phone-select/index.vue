<template lang="pug">
  .phone-select-container
      el-row(:gutter="20")
          el-col(:span="4")
              el-menu.brand-list(:default-active="currentBrand" @select="v => currentBrand = v")
                  el-menu-item(:index="0") 全部
                  el-menu-item(v-for="b in brand", :key="b.ctgid", :index="`${b.ctgid}`") {{b.ctgname}}
          el-col(:span="20")
              .phone-list
                  LocalTable(:data="currentPrds" ref="table", height="500")
                      el-table-column(type="selection" width="55")
                      el-table-column(prop="prdid", label="机型ID")
                      el-table-column(prop="prdname", label="机型名称")
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
            if (brandId === 0) {
                return this.prds
            }
            return this.prds.filter(p => p.ctgid === brandId)
        },
    },
    async created() {
        this.brand = await this.$store.getters.allCtg
        this.currentBrand = 0
        this.prds = this.$store.getters.allProduct
        this.tableComponent.$on('selection-change', this.selectChange)
    },
    methods: {
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
}
</style>
