import BaseService from '@/utils/BaseService'
class SysUserService extends BaseService {
    constructor() {
        super('sys-user')
    }
}
const target = new SysUserService()
export default target.proxy()
