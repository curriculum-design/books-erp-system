<template lang="pug">
.wang-editor-container(:class="{'full-screen': fullScreen}")
    .wangeditor(ref="wangeditor")
</template>

<script>
import Editor from './js/index.js'
let zIndex = 1000
export default {
    name: 'editor',
    props: ['value', 'editorConfig', 'customUpload'],
    data() {
        return {
            editor: {},
            currentContent: '',
            fullScreen: false,
        }
    },
    computed: {
        content: {
            get() {
                return this.value
            },
            set(val) {
                this.$emit('input', val)
            }
        },
    },
    watch: {
        content(val) {
            if (!this.editor.txt.text()) {
                this.editor.txt.html(val)
            }
        }
    },
    methods: {
        insertImages(imgUrls) {
            if (typeof imgUrls === 'string') {
                imgUrls = [imgUrls]
            }
            imgUrls.forEach(img => {
                this.editor.uploadImg.insertLinkImg(img)
            })
        },
        setContent(content) {
            this.content = content
            this.editor.txt.html(content)
        },
        getContent() {
            return this.content
        },
        getText() {
            return this.editor.txt.text()
        }
    },
    mounted() {
        this.$nextTick(() => {
            const editor = new Editor(this.$refs.wangeditor)
            if (this.customUpload) {
                editor.customConfig.handlerClickImage = imgUpload => {
                    if (typeof this.$selectImage === 'function') {
                        this.$selectImage({multiSelect: true}).then(imgs => {
                            imgs.forEach(img => {
                                imgUpload.insertLinkImg(img.url)
                            })
                        })
                    }
                }
            }
            editor.customConfig.fullScreen = () => {
                this.fullScreen = !this.fullScreen
            }
            editor.customConfig = {
                ...editor.customConfig,
                ...this.editorConfig,
                zIndex: zIndex,
            }
            editor.customConfig.onchange = (html) => {
                this.content = html
            }
            editor.create()
            editor.txt.html(this.value)
            this.editor = editor
        })
    }
}
</script>

<style lang="less">
@import './wang-editor.css';
.wang-editor-container{
    position: relative;
    &.mini{
        .wangeditor{
            height: 150px;
        }
    }
    .wangeditor {
        position: relative;
        height: 300px;
    }
    &.full-screen{
        height: 100%;
        width: 100%;
        position: fixed;
        left: 0;
        top: 0;
        .wangeditor{
            height: 100%;
        }
        z-index: 2000;
    }
    .w-e-text-container{
        &>.w-e-text{
            background: #fff;
        }
        .w-e-panel-container{
            top: 48px;
        }
    }
}
</style>
