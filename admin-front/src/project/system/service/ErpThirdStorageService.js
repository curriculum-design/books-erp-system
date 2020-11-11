import BaseService from '@/utils/BaseService'
class ErpThirdStorageService extends BaseService {
    constructor() {
        super('erp-third-storage')
    }
}
const target = new ErpThirdStorageService()
export default target.proxy()
