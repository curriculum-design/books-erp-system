import Const from '@/utils/Const'
export default {
    computed: {
        routerName() {
            return this.$route.name
        },
        delBtn() {
            return `${this.routerName}${Const.DELETE_SUFFIX}`
        },
        editBtn() {
            return `${this.routerName}${Const.EDIT_SUFFIX}`
        },
        createBtn() {
            return `${this.routerName}${Const.CREATE_SUFFIX}`
        },
        canDelete() {
            return this.$p(this.delBtn)
        },
        canEdit() {
            return this.$p(this.editBtn)
        },
        canCreate() {
            return this.$p(this.createBtn)
        }
    },
}
