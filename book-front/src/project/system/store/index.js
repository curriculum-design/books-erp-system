import Vue from 'vue'
import Vuex from 'vuex'
import actions from './actions.js'
import Types from './types.js'
import getters from './getters.js'
import LS from '@/utils/local-storage'

Vue.use(Vuex)

function getState() {
    return {
        systemInit: false,
        isLogin: false,
        user: {},
        permission: [],
        token: LS.get(Types.SYSTEM_TOKEN_KEY),
        allProduct: [],
        allChannel: [],
        allOrg: [],
        allSysParam: [],
        currentOrgId: '',
        unReadMessage: {},
        allowNotify: false,
        allDetails: [],
        currentMenu: {},
        visitCount: 0,
        todoNumber: 0,
        allPermission: [],
        loadAllPermissionPromise: null
    }
}

const store = new Vuex.Store({
    state: getState(),
    mutations: {
        [Types.CHANGE_MENU](state, menuData) {
            state.currentMenu = menuData
        },
        [Types.LOAD_UNREAD_MESSAGE](state, messagePage) {
            state.unReadMessage = messagePage
        },
        [Types.GET_USER_INFO](state, user) {
            state.user = user
        },
        [Types.LOGIN](state, token) {
            state.token = token
            state.isLogin = true
            LS.put(Types.SYSTEM_TOKEN_KEY, token)
        },
        [Types.EXIT](state, token) {
            store.replaceState(getState())
            LS.clear()
        },
    },
    actions,
    getters,
})

export default store
