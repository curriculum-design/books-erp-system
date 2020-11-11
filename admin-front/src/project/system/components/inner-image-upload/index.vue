<template lang="pug">
.inner-image-upload-container
    el-button(@click="openFile" size="mini", type="primary", :loading="fullscreenLoading") {{btntext}}
    input(ref="selectFile" type="file" accept="image/jpeg,image/png,image/gif" multiple="multiple" @change="upload")
</template>

<script>
export default {
    props: {
        btntext: {
            default: '上传'
        },
        data: {
            default() {
                return {}
            }
        }
    },
    data() {
        return {
            images: [],
            fullscreenLoading: false
        }
    },
    methods: {
        openFile() {
            this.$refs.selectFile.dispatchEvent(new MouseEvent('click'))
        },
        async upload(e) {
            let files = e.target.files || e.dataTransfer.files
            try {
                this.fullscreenLoading = true
                const result = []
                await Promise.all(Object.keys(files).map(async idx => {
                    const formData = new window.FormData()
                    formData.append('file', files[idx])
                    let imgUrl = await this.$nbImageService.save(formData)
                    result.push(imgUrl)
                    return imgUrl
                }))
                this.data.uploadResult = result
                this.$emit('uploadAfter', this.data)
                this.$refs.selectFile.value = ''
            } catch (e) {
                console.log(e)
                this.$message.error(e.msg)
            } finally {
                this.fullscreenLoading = false
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.inner-image-upload-container{
    display: inline-block;
    margin-left: @global-padding/3*2;
    margin-right: @global-padding/3*2;
    input {
        display: none;
    }
}
</style>
