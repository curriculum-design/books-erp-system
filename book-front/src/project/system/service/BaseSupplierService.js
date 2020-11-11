import BaseService from '@/utils/BaseService'
class BaseSupplierService extends BaseService {
    constructor() {
        super('base-supplier')
    }
}
const target = new BaseSupplierService()
export default target.proxy()
