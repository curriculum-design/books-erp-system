import TAdminService from '../service/SysAccountService'
import Sapi from '@/utils/Sapi'
import Types from './types'
// import Vue from 'vue'
// import AppVersionService from '../service/AppVersionService'
// import {MessageBox} from 'element-ui'
// async function appVersionListen() {
//     const {versionCode = 0} = (await AppVersionService.getLastByType({appType: 'BOSS'})) || {}
//     WebSocket.client.registerHandle(WebSocket.topic.APP_VERSION_BOSS, async (data) => {
//         const body = JSON.parse(data.body)
//         const h = new Vue().$createElement
//         if (body.versionCode > versionCode) {
//             await MessageBox({
//                 title: '有新的版本 ' + body.versionName + ' 可更新',
//                 message: h('pre', null, body.functions),
//                 showCancelButton: true,
//                 confirmButtonText: '更新版本',
//                 cancelButtonText: '暂不更新',
//             })
//             location.reload()
//         }
//     })
// }
export default {
    async [Types.GET_USER_INFO](store) {
        let token = store.getters.token
        Sapi.setToken(token)
        if (!token) {
            const errorCode = {
                code: 10001,
                msg: '登录已过期'
            }
            throw errorCode
        }
        try {
            let user = await TAdminService.info()
            store.commit(Types.GET_USER_INFO, user)
            // store.dispatch(Types.LOAD_ALL_CHANNEL)
            // WebSocket.initClient(token)
            // appVersionListen()
            return user
        } catch (e) {
            throw e
        }
    },
    [Types.CHANGE_CURRENT_ORG]({commit}, orgid) {
        commit(Types.CHANGE_CURRENT_ORG, orgid)
    },
    [Types.LOGIN](store, token) {
        store.commit(Types.LOGIN, token)
        return store.dispatch(Types.GET_USER_INFO)
    },
    [Types.EXIT]({commit}) {
        commit(Types.EXIT)
    },
    [Types.CHANGE_MENU]({commit}, data) {
        commit(Types.CHANGE_MENU, data)
    },
}
