import BaseService from '@/utils/BaseService'
class BaseStorageService extends BaseService {
    constructor() {
        super('base-storage')
    }
}
const target = new BaseStorageService()
export default target.proxy()
