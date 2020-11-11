<template lang="pug">
.import-dialog-with-button.button-group-margin
    ImportButton(:options="options", @inputData="inputDataHandler", :showTemplate="showTemplate", :src="templateSrc")
        slot
        slot(slot="export" name="export")
    ImportDialog(:data="inputData", :saveApi="saveApi", :show.sync="importDialogShow",
    :title="title", :successText="successText", :faildText="faildText", showTemplate, @complied="data => $emit('complied', data)")
</template>

<script>
import ImportButton from '../import-button'
import ImportDialog from '../import-dialog'
export default {
    props: {
        options: Object,
        saveApi: Function,
        showTemplate: {
            default: true
        },
        templateSrc: {
            type: String,
            default: ''
        },
        title: {default: '导入数据'},
        successText: {default: '导入成功'},
        faildText: {default: '导入失败'}
    },
    data() {
        return {
            inputData: [],
            importDialogShow: false,
        }
    },
    methods: {
        inputDataHandler(data) {
            this.inputData = data
            this.importDialogShow = true
        }
    },
    components: {ImportButton, ImportDialog}
}
</script>
<style lang="less" rel="stylesheet/less">
.import-dialog-with-button{
    display: inline;
}
</style>
