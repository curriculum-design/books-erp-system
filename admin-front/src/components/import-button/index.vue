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
  el-button.temp-btn-style(@click="downloadTemplate()" v-if="showTemplate")
    slot(name="export")
        i.fa.fa-cloud-download
        span 模板
</template>

<script>
import ExcelUtils from '@/utils/ExcelUtils'
export default {
    props: {
        src: {
            type: String,
            default: ''
        },
        options: {
            default() {
                return {}
            }
        },
        showTemplate: {
            default: true
        },
        isComparePrice: {
            default: false
        },
        exImportOptions: {
            default: () => {
                return []
            }
        }
    },
    methods: {
        async fileChange(e) {
            const file = this.$refs.upload.files[0]
            const data = await ExcelUtils.importFile(file, this.options)
            this.$emit('inputData', data)
            e.target.value = ''
        },
        downloadTemplate() {
            if (this.src) {
                this.funDownload(this.src, '示例导入模板')
                return
            }
            let exportData = {}
            if (!this.isComparePrice) {
                let {headerMapping} = this.options
                Object.keys(headerMapping).forEach(k => (exportData[k] = ''))
            } else {
                if (this.exImportOptions.length) {
                    this.exImportOptions.forEach(k => (exportData[k] = ''))
                }
            }
            ExcelUtils.jsonExport([exportData], {filename: '导入模板', onlyMapping: false})
        },
        funDownload(href) {
            let elemIF = document.createElement('iframe')
            elemIF.src = href
            elemIF.style.display = 'none'
            document.body.appendChild(elemIF)
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
    .temp-btn-style {
        margin-right: 20px;
    }
</style>
