import BaseService from '@/utils/BaseService'
class SysAccountService extends BaseService {
    constructor() {
        super('sys-admin')
    }
    info() {
        return this.get(`info`)
    }
    assign(form) {
        return this.post('assign', form)
    }
    roles(userid) {
        return this.get('roles', {userid})
    }
    findByRole(roleId) {
        return this.post('find-by-role', {roleId})
    }
}
const target = new SysAccountService()
export default target.proxy()
