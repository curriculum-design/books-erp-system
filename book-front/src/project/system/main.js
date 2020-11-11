import '@/utils/plugins.js'
import './service'
import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import ElementUI from 'element-ui'
import 'normalize.css'
import 'element-ui/lib/theme-chalk/index.css'
import RouterHook from './router/router-hook'
import Interceptor from '@/utils/Interceptor'
import PermissionPlugin from '@/utils/permission-plugin'
import MappingPlugin from '@/utils/mapping-plugin'
import FormatPlugin from '@/utils/format-plugin'
import DOMAIN from '@/utils/DOMAIN'
import 'font-awesome/css/font-awesome.css'
import Version from '@/../package.json'

document.title += ` - v${Version.version}`

Vue.use(ElementUI, {size: 'mini'})
RouterHook(router)
Vue.config.productionTip = false

if (DOMAIN.IS_DEV || DOMAIN.IS_TEST) {
    Vue.config.devtools = true
    Vue.config.silent = false
}

Vue.component('ChannelSelect', require('./components/channel-select'))
Vue.component('ContentBody', require('@/components/content-body'))
Vue.component('SearchTags', require('@/components/search-tags'))
Vue.component('DatetimeRange', require('@/components/datetime-range'))
Vue.component('LocalTable', require('@/components/local-table'))
Vue.component('ExportButton', require('@/components/export-button'))
Vue.component('ImportButton', require('@/components/import-button'))
Vue.component('ImportButtonData', require('@/components/import-button-data'))
Vue.component('ImportDialog', require('@/components/import-dialog'))
Vue.component('FileUpload', require('@/components/file-upload'))
Vue.component('CloseAbleImage', require('@/components/close-able-image'))
Vue.component('Editor', require('@/components/wang-editor'))
Vue.component('SelectImage', require('./components/select-image'))
Vue.component('UEditor', require('./components/ueditor'))
Vue.component('AddressSelect', require('./components/address-select'))
Vue.component('ExportAllButton', require('@/components/export-all-button'))
Vue.component('HiddenComponent', require('@/components/hidden-component'))
Vue.component('TreeTable', require('@/components/tree-table'))
Vue.component('InnerImageUpload', require('./components/inner-image-upload'))
Vue.component('InnerImageUploadIcon', require('./components/inner-image-upload-icon'))
Vue.component('ImportButtonWithDialog', require('@/components/import-button-with-dialog'))
Vue.component('PhoneSelectInput', require('./components/phone-select-input'))
Vue.component('SalesmanSelect', require('./components/salesman-select'))
Vue.component('PhoneSelectInputMultiple', require('./components/phone-select-input-multiple'))
Vue.nextTick(() => {
    require('./components/phone-select-dialog/index.js')
    require('./components/option-select-dialog/index.js')
    require('./components/goods-model-select-dialog/index.js')
    require('./components/take-photo/index.js')
    require('@/components/image-preview-dialog/index.js')
})

Vue.use(PermissionPlugin, {store})
Vue.use(MappingPlugin)
Vue.use(FormatPlugin)
// Vue.use(ExportHookPlugin, {store})

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: { App }
})

function errorMsg(data) {
    ElementUI.Message.error(data || '网络异常')
}

Interceptor.afterRequestHook((response = {}) => {
    let {body} = response
    let result = body
    if (result) {
        if (result.code !== 0) {
            switch (result.code) {
            case 40002:
                errorMsg('登录已过期，请重新登录')
                router.push({name: 'login'})
                break
            case 34012:
                ElementUI.MessageBox.alert(result.msg, '提示', {type: 'info'})
                break
            default:
                errorMsg(result.msg)
                break
            }
        }
    } else {
        switch (response.statusCode) {
        case 200:
            break
        case 404:
            errorMsg('404 NOT FOUND')
            break
        case 0:
            errorMsg('互联网链接中断，请检查网络情况')
            break
        case 504:
            errorMsg('无法连接到服务器，请检查网络情况')
            break
        default:
            console.error(response)
            errorMsg(response.statusText)
            break
        }
    }
})
