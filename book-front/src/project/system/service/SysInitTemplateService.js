import BaseService from '@/utils/BaseService'
class SysInitTemplate extends BaseService {
    constructor() {
        super('sys-init-template')
    }
}
const target = new SysInitTemplate()
export default target.proxy()
