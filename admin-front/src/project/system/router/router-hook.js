import store from '../store'
import Types from '../store/types'
import {MessageBox} from 'element-ui'
export default (router) => {
    router.beforeEach(async (to, from, next) => {
        if (to.meta.auth !== false && !store.getters.isLogin) {
            try {
                await store.dispatch(Types.LOGIN, store.getters.token)
            } catch (e) {
                next(false)
                if (e.code === 10001) {
                    router.replace({name: 'login', query: {redirect: location.origin + location.pathname + '#' + to.fullPath}})
                } else {
                    MessageBox.alert(e.msg).then(() => {
                        location.reload()
                    })
                }
                return false
            }
        }
        next(true)
    })
}
