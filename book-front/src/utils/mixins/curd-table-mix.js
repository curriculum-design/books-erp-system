import CurdMix from './curd-mix'
import BasePermissionMix from './base-permission-mix.js'
import TableMix from './table-mix'
export default (serviceName) => {
    return {
        mixins: [CurdMix(serviceName), BasePermissionMix, TableMix],
        data() {
            return {
                tableData: [],
                form: {},
                currentFilter: {},
                pageNum: 1,
                pageSize: 10,
                total: 0,
                loading: false,
                editShow: false,
                editForm: {},
                importDialogShow: false,
                importData: [],
            }
        },
        created() {
            this.initLoad()
        },
        methods: {
            inputData(data) {
                this.importDialogShow = true
                this.importData = data
            },
            initLoad() {
                this.loadData()
            },
            deleteFilter(key) {
                this.form[key] = Array.isArray(this.form[key]) ? [] : ''
                this.currentFilter[key] = Array.isArray(this.currentFilter[key]) ? [] : ''
                this.loadData(this.pageSize, this.pageNum, this.form)
            },
            async handleEdit(id) {
                let editForm = this.$options.data().editForm
                if (id !== undefined) {
                    this.loading = true
                    let loadEditForm = await this.get(id)
                    editForm = {...editForm, ...loadEditForm}
                }
                this.editForm = editForm
                this.loading = false
                this.editShow = true
            },
            deleteConfirm(id) {
                return this.delConfirm(id).then(() => {
                    this.loadData()
                })
            },
            reverConfirm(id) {
                return this.revConfirm(id).then(() => {
                    this.loadData()
                })
            },
            async loadData(pageSize = this.pageSize, pageNum = this.pageNum, params = this.currentFilter) {
                this.loading = true
                try {
                    let page = await this.list({pageSize, pageNum, ...params})
                    this.tableData = page.list
                    this.pageNum = page.pageNum
                    this.pageSize = page.pageSize
                    this.total = page.total
                    return page
                } catch (e) {
                    throw e
                } finally {
                    this.loading = false
                }
            },
            pageChangeHandler(page) {
                if (!this.loading) {
                    this.loadData(this.pageSize, page)
                }
            },
            sizeChangeHandler(size) {
                this.loadData(size)
            },
            filter(params) {
                this.currentFilter = {...params}
                this.loadData(this.pageSize, this.pageNum)
            },
        }
    }
}
