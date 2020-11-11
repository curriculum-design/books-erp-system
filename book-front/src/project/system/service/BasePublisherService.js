import BaseService from '@/utils/BaseService'
class BasePublisherService extends BaseService {
    constructor() {
        super('base-publisher')
    }
}
const target = new BasePublisherService()
export default target.proxy()
