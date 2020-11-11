import BaseService from '@/utils/BaseService'
class LoginService extends BaseService {
    constructor() {
        super('')
    }
    login(username, password) {
        return this.postJson('sys-admin/login', {username, password})
    }
    // loginByDingTalk(form) {
    //     return this.post('login-by-ding-talk', form)
    // }
}
export default new LoginService()
