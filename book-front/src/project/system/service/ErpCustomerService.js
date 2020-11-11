import BaseService from '@/utils/BaseService'
class ErpCustomerService extends BaseService {
    constructor() {
        super('erp-customer')
    }
}
const target = new ErpCustomerService()
export default target.proxy()
