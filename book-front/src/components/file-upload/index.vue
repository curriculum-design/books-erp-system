<template lang="pug">
el-upload.file-upload(
:class="{showlist: listType, 'hidden-self': hiddenSelf}"
:action="action"
:drag="drag"
:on-success="onSuccessHandler",
:on-progress="onProgress"
:on-error="onError"
:multiple="multiple"
:list-type="listType"
:show-file-list="showFileList"
:headers="headers"
:on-remove="onRemoveHandler"
:disabled="disabled || (loading && !multiple)")
    slot
        div
            i.el-icon-plus
            p(v-if="loading") 上传中 {{percent.toFixed(2)}}%
            p(v-else) 点击(拖拽)上传
</template>

<script>
export default {
    props: ['listType', 'showFileList', 'disabled', 'action', 'onSuccess', 'headers', 'onChange', 'onRemove', 'multiple', 'value', 'drag', 'hiddenSelf'],
    data() {
        return {
            loading: false,
            percent: 0
        }
    },
    methods: {
        onRemoveHandler(file, fileList) {
            this.onRemove && this.onRemove(file, fileList)
            this.changeFileList && this.changeFileList(fileList)
        },
        changeFileList(fileList) {
            this.$emit('fileChange', fileList)
        },
        onSuccessHandler(response, file, fileList) {
            if (!response.success) {
                this.onError({msg: response.msg})
                fileList.splice(fileList.indexOf(file), 1)
                return false
            }
            file.resultData = response.data
            if (typeof this.onSuccess === 'function') {
                this.onSuccess(response, file, fileList)
            }
            this.loading = false
            this.$emit('success', response)
            this.$emit('input', response.data)
            this.changeFileList(fileList)
        },
        onError(err = {}) {
            this.loading = false
            this.$message.error('上传图片失败' + err.msg)
            this.$emit('error', err)
        },
        onProgress({percent}) {
            this.percent = percent
            this.$emit('percent', percent)
            this.loading = true
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.file-upload{
    max-width: 100%;
    &.hidden-self{
        .el-upload-dragger{
            width: auto;
            height: auto;
            border: none;
            border-radius: 0px;
            background: none;
            text-align: inherit;
            overflow: hidden;
        }
        .el-upload{
            display: inline-block;
        }
    }
    &.showlist{
        .el-upload-list{
            display: block;
        }
    }
    .el-upload-list{
        display: none;
    }
    .el-upload--picture, .el-upload-dragger{
        max-width: 100%;
    }
    .el-upload{
        display: flex;
        align-items: center;
        justify-content: center;
        img{
            max-width: 100%;
            max-height: 100%;
        }
    }
}
</style>
