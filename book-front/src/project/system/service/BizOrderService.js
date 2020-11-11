import BaseService from '@/utils/BaseService'
class BizOrderService extends BaseService {
    constructor() {
        super('biz-order')
    }
}
const target = new BizOrderService()
export default target.proxy()
