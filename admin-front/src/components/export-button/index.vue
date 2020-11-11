<template lang="pug">
  el-button(type="info" @click="exportOut()", :loading="loading")
    i.fa.fa-download
    slot
        span {{isDifference ? '导出差异' : '导出'}}
</template>

<script>
import ExcelUtils from '@/utils/ExcelUtils'
export default {
    props: ['data', 'options', 'isDifference'],
    data() {
        return {
            loading: false
        }
    },
    methods: {
        async exportOut() {
            this.loading = true
            try {
                await ExcelUtils.jsonExportPromise(this.data, this.options)
            } finally {
                this.loading = false
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
