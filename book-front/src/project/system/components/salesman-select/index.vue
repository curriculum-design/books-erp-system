<template lang="pug">
el-select(:disabled="disabled" v-model="selectSalesId" filterable clearable remote placeholder="输入手机号进行添加", :remote-method="salesmanFilter" @change="changeHandler")
    el-option(v-for="item in optionsWithCurrentSales", :key="item.salesid", :label="item.mobile + '-' + item.salesname", :value="item.salesid")
</template>

<script>
export default {
    props: ['value', 'disabled'],
    data() {
        return {
            options: [],
            cache: {},
            currentSalesman: {},
            selectSalesId: '',
        }
    },
    watch: {
    },
    async created() {
        this.dataInit()
    },
    computed: {
        optionsWithCurrentSales() {
            if (this.currentSalesman.salesid) {
                return [...this.options, this.currentSalesman]
            }
            return this.options
        }
    },
    methods: {
        async dataInit() {
            if (this.value) {
                this.selectSalesId = this.value
                const salesman = await this.$api.bsSalesman.getById(this.value)
                this.currentSalesman = salesman
            }
        },
        setCache(salesman) {
            this.$set(this.cache, salesman.salesid, salesman)
        },
        async salesmanFilter(key) {
            if (key.length >= 5) {
                const data = await this.$api.bsSalesman.findSales({
                    mobile: key,
                    pageSize: 50
                })
                data.data.forEach(salesman => {
                    this.$set(this.cache, salesman.salesid, salesman)
                })
                this.options = data.data
            }
        },
        async changeHandler(salesId) {
            this.$emit('input', salesId)
            this.$emit('change', salesId)
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.nb-user-assign-channel-tree-select-container{
    height: 40vh;
    overflow: auto;
}
</style>
