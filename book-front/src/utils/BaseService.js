import Sapi from '@/utils/Sapi'
import Vue from 'vue'
export default class BaseService {
    constructor(url) {
        if (url && typeof url.indexOf === 'function' && url.indexOf('/') === 0) {
            this.baseUrl = url
        } else {
            this.baseUrl = Vue.config.httpRoot + url
        }
    }
    currentToken() {
        return Sapi.getToken()
    }
    authHeader() {
        return Sapi.authHeader()
    }
    buildUrl(url) {
        if (url && typeof url.indexOf === 'function' && url.indexOf('/') === 0) {
            return url
        }
        return `${this.baseUrl}/${url}`.replace('//', '/')
    }
    get(url, params) {
        return Sapi.get(this.buildUrl(url), params)
    }
    post(url, params) {
        return Sapi.post(this.buildUrl(url), params)
    }
    put(url, params) {
        return Sapi.put(this.buildUrl(url), params)
    }
    postJson(url, params) {
        return Sapi.postJson(this.buildUrl(url), params)
    }
    postPercent(url, params, percentCallback) {
        return Sapi.postPercent(this.buildUrl(url), params, percentCallback)
    }
    delRequest(url, params) {
        return Sapi.del(this.buildUrl(url), params)
    }
    save(params) {
        return Sapi.postJson(`${this.baseUrl}/save`, params)
    }
    update(params) {
        return Sapi.put(`${this.baseUrl}/update`, params)
    }
    del(id) {
        return Sapi.del(`${this.baseUrl}/${id}`)
    }
    delByIds(ids) {
        return Sapi.del(`${this.baseUrl}/ids`, {ids})
    }
    list(params) {
        return Sapi.get(`${this.baseUrl}/list`, params)
    }
    getById(id) {
        return Sapi.get(`${this.baseUrl}/${id}`)
    }
    toJSON() {
        return JSON.stringify(this)
    }
    proxy() {
        const target = this
        return new Proxy(target, {
            get(target, key) {
                return (...form) => {
                    if (target[key]) {
                        return target[key](...form)
                    }
                    const options = form[1] || {method: 'post'}
                    return target[options.method](key.replace(/([A-Z])/g, '-$1').toLowerCase(), ...form)
                }
            }
        })
    }
}
