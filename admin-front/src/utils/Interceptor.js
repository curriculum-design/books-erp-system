import Vue from 'vue'
Vue.prototype.$beforeRequest = Vue.prototype.$beforeRequest || []
Vue.prototype.$afterRequest = Vue.prototype.$afterRequest || []
const beforeRequestHook = Vue.prototype.$beforeRequest
let afterRequestHook = Vue.prototype.$afterRequest
export default {
    beforeRequestHook(fn) {
        beforeRequestHook.push(fn)
    },
    afterRequestHook(fn) {
        afterRequestHook = [...afterRequestHook, fn]
    },
    beforeRequest(data) {
        beforeRequestHook.forEach(fn => {
            if (typeof fn === 'function') {
                fn(data)
            } else {
                console.error(fn, ' is not a function')
            }
        })
    },
    afterRequest(data) {
        afterRequestHook.forEach(fn => {
            if (typeof fn === 'function') {
                fn(data)
            } else {
                console.error(fn, ' is not a function')
            }
        })
    }
}
