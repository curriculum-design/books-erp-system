<template lang="pug">
    ContentBody(title="图书管理")
        template(slot="filter")
            el-form(:inline="true", v-model="form" @submit.prevent.native="filter(form)")
                el-form-item(label="图书类型")
                    el-select(v-model="form.typeId" filterable clearable)
                        el-option(v-for="item in bookTypes", :key="item.id", :label="item.name", :value="item.id")
                el-form-item(label="出版社")
                    el-select(v-model="form.publisherId" filterable clearable)
                        el-option(v-for="item in publishers", :key="item.id", :label="item.name", :value="item.id")
                el-form-item(label="图书编号")
                    el-input(v-model="form.code")
                el-form-item(label="图书名称")
                    el-input(v-model="form.name")
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
        EditForm(:show.sync="editShow", v-model="editForm", @submit="submitHandler" v-loading.body="editLoading" :bookTypes="bookTypes" :publishers="publishers")
</template>

<script>
import CurdTableMix from '@/utils/mixins/curd-table-mix.js'
import {mapGetters} from 'vuex'
import EditForm from './form'
import CommonUtils from '../../../../utils/CommonUtils'
export default {
    components: {EditForm},
    mixins: [CurdTableMix('$baseBookService')],
    data() {
        return {
            bookTypes: [],
            publishers: [],
            editForm: {
                id: '',
                createTime: '',
                updateTime: '',
                createUser: '',
                isDel: '',
                code: '',
                name: '',
                typeId: '',
                publisherId: '',
            }
        }
    },
    async created() {
        this.bookTypes = await this.$baseBookTypeService.all({}, {method: 'get'})
        this.publishers = await this.$basePublisherService.all({}, {method: 'get'})
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
                code: {
                    label: '图书编号'
                },
                name: {
                    label: '图书名称'
                },
                typeId: {
                    label: '图书类型',
                    format: (v, r) => {
                        return CommonUtils.arrayToMap(this.bookTypes, 'id', 'name')[v]
                    }
                },
                publisherId: {
                    label: '出版社',
                    format: (v, r) => {
                        return CommonUtils.arrayToMap(this.publishers, 'id', 'name')[v]
                    }
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
