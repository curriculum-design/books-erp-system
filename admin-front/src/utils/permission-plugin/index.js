export default {
    install(Vue, {store}) {
        Vue.mixin({
            methods: {
                $isAdmin() {
                    let userInfo = store.getters.user
                    return userInfo.superAdmin
                },
                $p(permission) {
                    return this.$isAdmin() || store.getters.permissionSigns.indexOf(permission) > -1
                },
            }
        })
    }
}
