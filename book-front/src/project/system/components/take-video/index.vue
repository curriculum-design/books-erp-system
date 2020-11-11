<template lang="pug">
.take-video-container
    video.take-video(ref="inVideo" :width="width" :height="height")
    .icon-video-recoding
        .text-danger
            slot
</template>

<script>
export default {
    props: {
        recorderSuccess: {
            default() {
                return () => {}
            }
        },
        width: {
            default: 925
        },
        height: {
            default: 520
        },
        videoOption: {
            default() {
                return {
                    width: 1280,
                    height: 720
                }
            }
        },
        audioOption: {
            default: true
        }
    },
    data() {
        return {
            constraints: {},
            blobData: '',
            mediaRecorder: null,
            status: 1,    // 1未录制2录制中3录制结束
            loading: false,
            isCancel: false,
            recoding: false
        }
    },
    mounted() {
        this.constraints
        window.navigator.getUserMedia = window.navigator.getUserMedia || window.navigator.webkitGetUserMedia || window.navigator.mozGetUserMedia
        this.init()
    },
    methods: {
        init() {
            navigator.mediaDevices.getUserMedia({
                audio: this.audioOption,
                video: this.videoOption
            }).then(mediaStream => {
                let video = this.$refs.inVideo
                video.srcObject = mediaStream
                video.onloadedmetadata = function(e) {
                    video.play()
                }
                //  赋值 video 并开始播放
                video.srcObject = mediaStream
                this.mediaRecorder = new window.MediaRecorder(mediaStream)
                this.mediaRecorder.ondataavailable = async (blob) => {
                    if (this.isCancel) {
                        return
                    } else {
                        this.recorderSuccess(blob)
                    }
                }
            })
        },
        begin() {
            this.status = 2
            this.isCancel = false
            this.mediaRecorder.start()
            this.recoding = true
        },
        end() {
            this.mediaRecorder.stop()
            this.recoding = false
        },
        cancel() {
            this.isCancel = true
            this.end()
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.take-video-container{
    video {
        background: #000;
    }
    position: relative;
    .icon-video-recoding {
        position: absolute;
        left: 10px;
        top: 10px;
    }
}
</style>
