<template lang="pug">
.file-upload-button-container
    //- el-tag.file-tag(v-if="value" closable @close="$emit('input', '')") {{showFileName}}
    el-input(v-model="showFileName" readonly clearable)
        el-button(@click="openFile" size="mini", type="primary", :loading="loading" slot="append") {{buttonText}}
    a(v-show="false" :href="value")
    input(ref="selectFile" type="file" :accept="accept" @change="upload")
</template>

<script>
export default {
    props: {
        buttonText: {
            default: '选择文件'
        },
        value: {
            default() {
                return {}
            }
        },
        accept: {
            default: '*'
        }
    },
    data() {
        return {
            tempFileName: '',
            loading: false
        }
    },
    computed: {
        showFileName: {
            get() {
                if (this.value) {
                    return this.tempFileName || this.value.substring(this.value.lastIndexOf('/') + 1)
                }
                return ''
            },
            set(val) {
                this.$emit('input', val)
            }
        }
    },
    methods: {
        openFile() {
            this.$refs.selectFile.dispatchEvent(new MouseEvent('click'))
        },
        async upload(e) {
            let files = e.target.files || e.dataTransfer.files
            try {
                this.loading = true
                const formData = new window.FormData()
                const file = files[0]
                formData.append('file', file)
                let fileUrl = await this.$nbImageService.save(formData)
                this.$emit('input', fileUrl)
                this.tempFileName = file.name
                this.$message.success('上传成功')
                this.$refs.selectFile.value = ''
            } finally {
                this.loading = false
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.file-upload-button-container{
    display: inline-block;
    width: 100%;
    .file-tag {
        margin-right: 10px;
    }
    input {
        display: none;
    }
}
</style>
