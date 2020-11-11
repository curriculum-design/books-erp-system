<template lang="pug">
    el-button-group.button-group-margin
        el-button(type="info" @click="$refs.upload.click()")
            input(type="file"
            style="display: none;"
            ref="upload"
                @change="fileChange"
            accept=".xls,.xlsx"
            )
            i.fa.fa-upload
            slot
                span 导入
        el-button(@click="downloadTemplate()")
            slot(name="export")
                i.fa.fa-cloud-download
                span 模板
</template>

<script>
    import ExcelUtils from '@/utils/ExcelUtils'
    export default {
        props: ['options', 'exportOptions', 'data'],
        methods: {
            async fileChange(e) {
                const file = this.$refs.upload.files[0]
                const data = await ExcelUtils.importFile(file, this.options)
                this.$emit('inputData', data)
                e.target.value = ''
            },
            async downloadTemplate() {
                this.loading = true
                try {
                    await ExcelUtils.jsonExportPromise(this.data, this.exportOptions, '导入模板')
                } finally {
                    this.loading = false
                }
            }
        }
    }
</script>
<style lang="less" rel="stylesheet/less">
</style>
