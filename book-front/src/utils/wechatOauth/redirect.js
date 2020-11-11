import DOMAIN from '@/utils/DOMAIN.js'
import Query from 'query-string'

// callback 域名需要后端校验是否是自己的域名
function getAuthRedirectUrl(callback, wechatCode, scope) {
    let env = '/pro'
    if (DOMAIN.IS_DEV) {
        env = '/dev'
    } else if (DOMAIN.IS_TEST) {
        env = '/test'
    } else if (DOMAIN.IS_LOCAL_DEV) {
        env = '/test' // TODO 开发 对应的调试环境
    }
    return `https://m.shanhs.com/sapi/gateway/open-api/v1/wechat/authorized${env}?wechatCode=${wechatCode}&callback=${callback}&scope=${scope}`
}

export default (wechatCode, scope = 'snsapi_base') => {
    let callback = window.location.href
    if (callback.indexOf('tempToken') > -1) {
        let query = Query.parse(window.location.search)
        delete query.tempToken
        delete query.tokenType
        delete query.tokenCode
        console.log(query)
        let searchArray = []
        Object.keys(query).forEach((key) => {
            searchArray.push(`${key}=${query[key]}`)
        })
        let search = searchArray.join('&')
        search = search ? `?${search}` : ``
        callback = window.location.origin + window.location.pathname + search + window.location.hash
    }
    let url = `${getAuthRedirectUrl(encodeURIComponent(callback), wechatCode, scope)}`
    window.location.href = url
}
