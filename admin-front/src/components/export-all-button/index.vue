<template lang="pug">
  el-button(type="info" @click="exportOut()", :loading="loading")
    i.fa.fa-download
    slot
        span 导出全部
</template>

<script>
import ExcelUtils from '@/utils/ExcelUtils'
export default {
    props: ['api', 'options', 'total', 'filter', 'allApi'],
    data() {
        return {
            loading: false,
        }
    },
    methods: {
        async exportOut() {
            this.loading = true
            try {
                if (this.allApi) {
                    const data = await this.allApi()
                    await ExcelUtils.jsonExportPromise(data, this.options)
                } else {
                    let pageSize = 300
                    let page = Math.floor((this.total + pageSize - 1) / pageSize)
                    let allData = []
                    for (let i = 0; i < page; i++) {
                        const {data} = await this.api({
                            ...this.filter,
                            pageSize,
                            pageNum: i + 1,
                        })
                        allData = [...allData, ...data]
                    }
                    await ExcelUtils.jsonExportPromise(allData, this.options)
                }
            } catch (e) {
                console.log(e)
            } finally {
                this.loading = false
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
