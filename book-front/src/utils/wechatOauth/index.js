import Query from 'query-string'
import Es6Promise from 'es6-promise'
import LS from '@/utils/local-storage'
import UserService from '@/project/system/service/OpenApiService.js'
import Redirect from '@/utils/wechatOauth/redirect.js'
Es6Promise.polyfill()

const SALESMAN = 'salesman' // salesman 合作平台
const MANAGE = 'manage' // manage 管理平台
const SHANS = 'shanhs' // shanhs 闪回收
const SHANYU = 'shanyu' // shanyu 闪鱼

// 判定是微信浏览器
let ua = navigator.userAgent.toLowerCase()
let isWeixin = ua.indexOf('micromessenger') !== -1

let {tempToken, tokenCode} = Query.parse(window.location.search)
function baseWechatAuth(wechatCode, scope, notInwechat = false) {
    return new Promise(function(resolve, reject) {
        let tokenOpenId = LS.get(wechatCode + 'OpenIdToken')
        if (tokenOpenId) {
            resolve(tokenOpenId)
        } else if ((isWeixin || notInwechat) && (!tempToken || !!tokenCode && (tokenCode !== wechatCode))) {
            Redirect(wechatCode, scope)
        } else if (tempToken) {
            UserService.getOpenIdToken(tokenCode, tempToken).then(({openIdToken, wechatUserInfo}) => {
                LS.put(tokenCode + 'OpenIdToken', openIdToken, 23 / 24)
                LS.put('wechatUserInfo', wechatUserInfo)
                LS.put(tokenCode + 'WechatUserInfo', wechatUserInfo)
                resolve(openIdToken)
            }).catch((e) => {
                reject(e)
            })
        } else {
            resolve()
        }
    })
}

// 默认不跳转获取tokenOpenId
function initOpenId(wechatCode) {
    return new Promise(function(resolve, reject) {
        let tokenOpenId = LS.get(wechatCode + 'OpenIdToken')
        if (tokenOpenId) {
            resolve(tokenOpenId)
        } else if (tempToken && tokenCode === wechatCode) {
            UserService.getOpenIdToken(wechatCode, tempToken)
            .then(
                ({ openIdToken, wechatUserInfo }) => {
                    LS.put(
                        wechatCode + 'OpenIdToken',
                        openIdToken,
                        23 / 24
                    )
                    LS.put(
                        'wechatUserInfo',
                        wechatUserInfo
                    )
                    resolve(openIdToken)
                }
            )
            .catch(e => {
                reject(e)
            })
        } else {
            resolve()
        }
    })
}

export default {
    initOpenId: initOpenId,
    shanhsWechatAuth(scope = 'snsapi_base') {
        return baseWechatAuth(SHANS, scope)
    },
    shanyuWechatAuth(scope = 'snsapi_base') {
        return baseWechatAuth(SHANYU, scope)
    },
    manageWechatAuth(scope = 'snsapi_base') {
        return baseWechatAuth(MANAGE, scope, true)
    },
    salesmanWechatAuth(scope = 'snsapi_base') {
        return baseWechatAuth(SALESMAN, scope)
    },
    commonWechatAuth(wechatCode, scope = 'snsapi_base') {
        return baseWechatAuth(wechatCode, scope)
    }
}
