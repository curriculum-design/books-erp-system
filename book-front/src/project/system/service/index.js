import Vue from 'vue'
import BaseService from '@/utils/BaseService'

Vue.config.httpRoot = '/management-system-api/v1/'

Vue.prototype.$api = new Proxy({}, {
    get(target, key) {
        return new BaseService(key.replace(/([A-Z])/g, '-$1').toLowerCase()).proxy()
    }
})

Vue.prototype.$loginService = require('./LoginService.js').default

Vue.prototype.$erpCustomerService = require('./ErpCustomerService.js').default
Vue.prototype.$erpCustomerAttachmentService = require('./ErpCustomerAttachmentService.js').default
Vue.prototype.$erpThirdStorageService = require('./ErpThirdStorageService.js').default
Vue.prototype.$sysInitTemplateService = require('./SysInitTemplateService.js').default
Vue.prototype.$sysUserService = require('./SysUserService.js').default
Vue.prototype.$bizOrderService = require('./BizOrderService.js').default
Vue.prototype.$baseBookService = require('./BaseBookService.js').default
Vue.prototype.$baseBookTypeService = require('./BaseBookTypeService.js').default
Vue.prototype.$basePublisherService = require('./BasePublisherService.js').default
Vue.prototype.$baseSupplierService = require('./BaseSupplierService.js').default
Vue.prototype.$baseClientService = require('./BaseClientService.js').default
Vue.prototype.$baseStorageService = require('./BaseStorageService.js').default
