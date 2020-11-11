import BaseService from '@/utils/BaseService'
class BaseBookTypeService extends BaseService {
    constructor() {
        super('base-book-type')
    }
}
const target = new BaseBookTypeService()
export default target.proxy()
