import BaseService from '@/utils/BaseService'
class BaseBookService extends BaseService {
    constructor() {
        super('base-book')
    }
}
const target = new BaseBookService()
export default target.proxy()
