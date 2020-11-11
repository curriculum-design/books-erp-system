<template lang="pug">
    ContentBody(title="客户管理")
        template(slot="filter")
            el-form(:inline="true", v-model="form" @submit.prevent.native="filter(form)")
                el-form-item(label="客户名称")
                    el-input(v-model="form.name")
                el-form-item(label="联系人名称")
                    el-input(v-model="form.contactName")
                el-form-item(label="联系人手机号")
                    el-input(v-model="form.contactMobile")
                el-form-item
                    el-button(type="primary", native-type="submit") 查询
                    el-button(type="success" @click="handleEdit()") 新增
        el-table(:data="tableData" v-loading="loading" border)
            el-table-column(:prop="key", :key="key", :label="value.label || value", v-for="value, key in headerMapping", :show-overflow-tooltip="true", :width="value.width")
                ColumnContent(slot-scope="{row}" :columnDefine="value", :row="row", :value-key="key" :renderContent="value.render")
            el-table-column(label="操作" min-width="120")
                template(slot-scope="scope")
                    .icon-btn.el-icon-edit-outline(@click="handleEdit(scope.row.id)")
                    .icon-btn.el-icon-delete.danger(type="danger", @click="deleteConfirm(scope.row.id)")
        template(slot="footer")
            el-pagination(:pageNum.sync="pageNum", :total="total", :pageSize="pageSize",@current-change="pageChangeHandler" @size-change="sizeChangeHandler" layout="total, sizes, prev, pager, next")
        EditForm(:show.sync="editShow", v-model="editForm", @submit="submitHandler" v-loading.body="editLoading")
</template>

<script>
import CurdTableMix from '@/utils/mixins/curd-table-mix.js'
import {mapGetters} from 'vuex'
import EditForm from './form'
export default {
    components: {EditForm},
    mixins: [CurdTableMix('$baseClientService')],
    data() {
        return {
            editForm: {
                id: '',
                createTime: '',
                updateTime: '',
                createUser: '',
                isDel: '',
                name: '',
                contactName: '',
                contactMobile: '',
            }
        }
    },
    methods: {
        submitHandler(val) {
            this.submit(val, 'id').then(data => {
                this.editShow = false
                this.loadData()
            })
        }
    },
    computed: {
        ...mapGetters['systemMapping'],
        headerMapping() {
            return {
                name: {
                    label: '客户名称'
                },
                contactName: {
                    label: '联系人名称'
                },
                contactMobile: {
                    label: '联系人手机号'
                },
                createTime: {
                    label: '创建时间'
                },
                createUser: {
                    label: '创建员工'
                },
            }
        },
        exportOptions() {
            return {
                headerMapping: this.headerMapping
            }
        },
        importOptions() {
            return {
                headerMapping: {
                }
            }
        }
    },
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
