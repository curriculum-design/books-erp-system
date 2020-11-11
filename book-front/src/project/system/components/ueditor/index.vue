<template lang="pug">
  .ueditor-container(:id="randomId" v-loading='loading' element-loading-text="编辑器加载中")
</template>

<script>
let UEINIT = false
import Sapi from '@/utils/Sapi'
export default {
    props: ['value'],
    computed: {
        randomId() {
            return 'UEditor_ID_' + Date.now()
        },
        content: {
            get() {
                return this.value
            },
            set(val) {
                this.$emit('input', val)
            },
        }
    },
    data() {
        return {
            loading: true,
        }
    },
    methods: {
        setContent(content = '') {
            this.LoadPromise.then(() => {
                this.content = content
                this.editor.setContent(content)
            })
        },
        getText() {
            return this.editor && this.editor.getContentTxt && this.editor.getContentTxt()
        }
    },
    mounted() {
        this.LoadPromise = new Promise((resolve, reject) => {
            this.$nextTick(async () => {
                require.ensure([], () => {
                    require('@/../static/ueditor-dist/ueditor.config.js')
                    require('@/../static/ueditor-dist/ueditor.all.js')
                    require('@/../static/ueditor-dist/lang/zh-cn/zh-cn.js')
                    const UE = window.UE
                    if (!UEINIT) {
                        UEINIT = true
                        UE.ui['image-select'] = (editor, uiName) => {
                            var btn = new UE.ui.Button({
                                name: 'image-select',
                                title: '选择/上传图片',
                                // 需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
                                className: 'edui-for-simpleupload',
                                // 点击时执行的命令
                                onclick: () => {
                                    // 这里可以不用执行命令,做你自己的操作也可
                                    this.$selectImage({
                                        multiSelect: true
                                    }).then(images => {
                                        editor.focus()
                                        images.forEach(data => {
                                            editor.execCommand('insertimage', {
                                                src: data.url
                                            })
                                        })
                                    })
                                }
                            })

                            // 因为你是添加button,所以需要返回这个button
                            return btn
                        }
                    }
                    this.editor = UE.getEditor(this.randomId, {
                        'imageActionName': 'uploadimage',
                        'imageFieldName': 'file',
                        'imageAllowFiles': ['.png', '.jpg', '.jpeg', '.gif', '.bmp'],
                        'imageCompressEnable': true,
                        'imageCompressBorder': 1600,
                        'imageInsertAlign': 'none',
                        'imageUrlPrefix': '',
                        'imagePathFormat': '/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}',
                        imageMaxSize: 2048000 * 2,
                        imageUploadHeaders: Sapi.authHeader(),
                        autoHeightEnabled: false
                    })
                    this.editor.ready(() => {
                        resolve()
                        this.setContent(this.content || '')
                        this.editor.addListener('contentChange', (val) => {
                            this.content = this.editor.getContent()
                        })
                    })
                    this.loading = false
                }, (e) => {
                    console.error(e)
                    reject(e)
                })
            })
        })
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.ueditor-container {
    line-height: 16px;
    height: 400px;
    margin-bottom: 100px;
}
</style>
