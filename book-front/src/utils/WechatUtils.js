import Sapi from './Sapi'
let isInMiniProgram = false
const isMinProgramPromise = new Promise((resolve, reject) => {
    window.wx.miniProgram.getEnv((res) => {
        isInMiniProgram = res.miniprogram
        if (res.miniprogram) {
            resolve(res.miniprogram)
        } else {
            reject()
        }
    })
    setTimeout(() => {
        reject()
    }, 5000)
})

const OFFLINE_APP_ID = 'wx163b83c8fcd12e4d'

let wechatReady = new Promise((resolve, reject) => {
    const wx = window.wx
    Sapi.postJson('/sapi/api/weixin/salesman/jsConfigSign', {
        url: location.href.split('#')[0]
    }).then(({
                result: sign
            }) => {
        let config = { ...sign,
            appId: OFFLINE_APP_ID,
            jsApiList: [
                'scanQRCode',
                'chooseImage',
                'uploadImage',
                'chooseWXPay'
            ]
        }
        wx.config(config)
        wx.ready(() => {
            resolve(wx)
        })
        wx.error(res => {
            reject(res)
            console.error('wechat init faild', res)
        })
    })
})
export default {
    isInMiniProgram,
    isMinProgramPromise,
    isInWechat: window.navigator.userAgent.indexOf('MicroMessenger') > -1,
    chooseFileHandle() {
        return new Promise((resolve, reject) => {
            wechatReady.then(wx => {
                wx.chooseImage({
                    count: 1, // 默认9
                    sizeType: ['original'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: (res) => {
                        let localIds = res.localIds // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        localIds.forEach(localId => {
                            wx.uploadImage({
                                localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
                                isShowProgressTips: 1, // 默认为1，显示进度提示
                                success: (res) => {
                                    let serverId = res.serverId // 返回图片的服务器端ID
                                    Sapi.postJson('/sapi/api/weixin/upload/saveWechatImageCommonHandle', {
                                        serverId,
                                        imgPath: 'customer-idcard'
                                    }).then(({
                                        imgUrl
                                    }) => {
                                        resolve(imgUrl)
                                    }).catch((e) => {
                                        reject(e)
                                    })
                                },
                                cancel: () => {
                                    reject()
                                }
                            })
                        })
                    },
                    cancel: () => {
                        reject()
                    }
                })
            })
        })
    }
}
