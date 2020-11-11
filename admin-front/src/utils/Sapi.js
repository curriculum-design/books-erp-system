/**
 * Created by linlx on 2016/5/16.
 */
import Request from './Request'
import Interceptor from './Interceptor'
import Const from './Const'
function AuthorizationHeader(token) {
    return {
        [Const.AUTHORIZATION_HEADER]: token
    }
}

function requestProcess(thenable) {
    return new Promise(function(resolve, reject) {
        thenable.then((response) => {
            Interceptor.afterRequest(response)
            let data = response.body
            if (response.status !== 200) {
                reject(response)
            } else if (data.code === 0) {
                resolve(data.data)
            } else {
                reject(data)
            }
        }).catch(({status, message, response}) => {
            Interceptor.afterRequest(response)
            console.error(response)
            reject({code: status, msg: message})
        })
    })
}

let token = ''

export default {
    setToken(tokenData) {
        token = tokenData
    },
    getToken() {
        return token
    },
    authHeader() {
        return AuthorizationHeader(token)
    },
    baseRequest(type, url, params, headers, callback) {
        Interceptor.beforeRequest({type, url, params, headers})
        return Request[type](url, params, headers, callback)
    },
    postJson(url, params) {
        return requestProcess(this.baseRequest('postJson', url, params, AuthorizationHeader(token)))
    },
    postPercent(url, params, percentCallback) {
        return requestProcess(this.baseRequest('postPercent', url, params, AuthorizationHeader(token), percentCallback))
    },
    post(url, params) {
        return requestProcess(this.baseRequest('post', url, params, AuthorizationHeader(token)))
    },
    put(url, params) {
        return requestProcess(this.baseRequest('putJson', url, params, AuthorizationHeader(token)))
    },
    del(url, params) {
        return requestProcess(this.baseRequest('del', url, params, AuthorizationHeader(token)))
    },
    get(url, params) {
        return requestProcess(this.baseRequest('get', url, params, AuthorizationHeader(token)))
    },
}
