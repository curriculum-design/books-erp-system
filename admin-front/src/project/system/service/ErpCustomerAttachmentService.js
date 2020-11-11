import BaseService from '@/utils/BaseService'
class ErpCustomerAttachmentService extends BaseService {
    constructor() {
        super('erp-customer-attachment')
    }
}
const target = new ErpCustomerAttachmentService()
export default target.proxy()
