<template lang="pug">
  el-dialog.take-photo-dialog(:visible.sync="dialogShow" title="拍照", :append-to-body="true")
      DrawDialog(:show.sync="showEdit", v-if="showEdit", :image="currentImage.preview", @save="data => editImageSave(data, currentImage)")
      el-row
          el-col(:span="18")
              .video-container
                  video(ref="video")
          el-col.image-list-container(:span="6")
              el-input(placeholder="点击粘贴图片" @paste.native="pasteEvent")
              .image-list
                  .image-container(v-for="img, index in images"
                  v-loading="img.status === 1", :element-loading-text="`上传中${img.percent}%`")
                      img(:src="img.preview")
                      el-button(type="primary" @click="editImage(img)") 编辑
                      el-button(type="danger" @click="removeImage(index)") 删除
      .opeator-buttons
          el-button(type="primary" @click="takePhoneHandler" v-if="allowVideo", :loading="loading || finishLoading") 拍照
          el-button(@click="finish", :loading="finishLoading") 完成
</template>

<script>
import formDialogMix from '@/utils/mixins/form-dialog-mix'
import DrawDialog from './draw-dialog'
import CommonUtils from '@/utils/CommonUtils'
const IMAGE_STATUS_NORMAL = 0
const IMAGE_STATUS_UPLOADING = 1
const IMAGE_STATUS_SUCCESS = 2
export default {
    mixins: [formDialogMix],
    components: {DrawDialog},
    props: {
        editEnable: {default: true}
    },
    data() {
        return {
            showEdit: false,
            currentImage: '',
            allowVideo: false,
            images: [],
            loading: false,
            finishLoading: false,
            uploadingStatusll: {},
        }
    },
    watch: {
        dialogShow(val) {
            if (val) {
                this.initVideo()
            }
        }
    },
    computed: {
        videoDom() {
            return this.$refs.video
        },
    },
    methods: {
        pasteEvent(e) {
            var items = (event.clipboardData || event.originalEvent.clipboardData).items
            for (let index in items) {
                let item = items[index]
                if (item.kind === 'file') {
                    let blob = item.getAsFile()
                    this.addImage(blob)
                }
            }
        },
        editImageSave(data, image) {
            if (data) {
                image.preview = data
                image.blob = CommonUtils.dataURItoBlob(data)
            }
        },
        editImage(img) {
            this.currentImage = img
            this.showEdit = true
        },
        async finish() {
            this.finishLoading = true
            try {
                const allImages = await this.uploadAllImages()
                this.$emit('success', allImages.map(d => d.url))
                this.dialogShow = false
            } finally {
                this.finishLoading = false
            }
            // this.$emit('success', this.images)
            // this.dialogShow = false
        },
        async uploadAllImages() {
            const allPromiseArray = this.images.map(async image => {
                image.status = IMAGE_STATUS_UPLOADING
                let {blob} = image
                const formData = new window.FormData()
                formData.append('file', blob)
                const data = await this.$nbImageService.save(formData, event => {
                    image.percent = (event.percent && event.percent.toFixed(2)) || 100
                })
                image.url = data
                image.status = IMAGE_STATUS_SUCCESS
                return image
            })
            return await Promise.all(allPromiseArray)
        },
        removeImage(index) {
            this.images.splice(index, 1)
        },
        takePhoneHandler() {
            this.loading = true
            const canvas = document.createElement('canvas')
            let {clientHeight, clientWidth} = this.videoDom
            let drawHeight = clientHeight * 2
            let drawWidth = clientWidth * 2
            canvas.height = drawHeight
            canvas.width = drawWidth
            canvas.getContext('2d').drawImage(this.videoDom, 0, 0, drawWidth, drawHeight)
            canvas.toBlob(blob => {
                this.addImage(blob)
                this.loading = false
                // 添加单张照片拍摄(卢玮：2018-01-08)
                if (this.single) {
                    this.finish()
                }
            }, 'image/jpeg', 0.8)
        },
        addImage(blob) {
            this.images.push({
                blob: blob,
                percent: 0,
                preview: URL.createObjectURL(blob),
                status: IMAGE_STATUS_NORMAL,
                url: '',
            })
        },
        initVideo() {
            if (this.dialogShow) {
                this.loading = false
                this.finishLoading = false
                this.images = []
                this.$nextTick(() => {
                    this.allowVideo = false
                    navigator.getUserMedia({video: true}, stream => {
                        this.videoDom.src = URL.createObjectURL(stream)
                        this.videoDom.play()
                        this.allowVideo = true
                    }, e => {
                        this.$alert('调用摄像头失败，请检查设备是否链接，并确认是否授权摄像头访问权限')
                    })
                })
            }
        }
    },
    mounted() {
        this.initVideo()
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.take-photo-dialog{
    video{
        width: 100%;
    }
    .image-list-container{
        padding-left: @global-padding/2;
        .image-container{
            position: relative;
            margin-bottom: @global-padding;
            img{
                display: block;
            }
        }
    }
    .opeator-buttons{
        text-align: right;
    }
}
</style>
