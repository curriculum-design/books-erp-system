import BaseService from '@/utils/BaseService'
class BaseClientService extends BaseService {
    constructor() {
        super('base-client')
    }
}
const target = new BaseClientService()
export default target.proxy()
