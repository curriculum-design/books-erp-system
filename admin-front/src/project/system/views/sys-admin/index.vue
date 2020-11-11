<template lang="pug">
  ContentBody.nb-user-container(title="员工管理")
      template(slot="filter")
          el-form(:inline="true", v-model="form" @submit.prevent.native="filter(form)")
              el-form-item(label="用户名")
                  el-input(v-model="form.userid")
              el-form-item(label="用户姓名")
                  el-input(v-model="form.username")
              el-form-item(label="状态")
                  el-select(v-model="form.status")
                      el-option(v-for="key, value in $mapping.status", :label="value", :value="key", :key="value") {{value}}
              el-form-item(label="手机")
                  el-input(v-model="form.phone")
              el-form-item
                  el-button(type="primary", native-type="submit") 查询
                  el-button(type="success" @click="handleEdit()" v-if="canCreate") 新增
          SearchTags(:filter="currentFilter" @close="deleteFilter")
      el-table(:data="tableData" v-loading="loading")
          el-table-column(:prop="key", :key="key", :label="value.label || value", v-for="value, key in headerMapping", :width="value.width")
              template(slot-scope="scope")
                  span {{fieldParse(value, scope.row[key])}}
          el-table-column(label="操作" min-width="300")
              template(slot-scope="scope")
                .button-cell
                  el-button(size="mini",  @click="handleEdit(scope.row.id)" v-if="canEdit") 编辑
                  el-button(size="mini",  @click="handleAssign(scope.row.id)" v-if="$p(ssys-Adminn)") 分配角色
      template(slot="footer")
          el-pagination(:pageNum.sync="pageNum", :total="total", :pageSize="pageSize",@current-change="pageChangeHandler" @size-change="sizeChangeHandler" layout="total, sizes, prev, pager, next")
      EditForm(:show.sync="editShow",v-if="editShow" v-model="editForm", @submit="submitHandler" v-loading.body="editLoading")
      AssignDialog(:show.sync="assignShow", v-model="assignForm")
</template>

<script>
import CurdTableMix from '@/utils/mixins/curd-table-mix.js'
import AssignDialog from './assign'
export default {
    components: {EditForm: require('./form'), AssignDialog},
    mixins: [CurdTableMix('$tAdminService')],
    data() {
        return {
            assignForm: {},
            assignShow: false,
            channelSelectType: 'button',
            editForm: {
            },
            id: '',
        }
    },
    methods: {
        async handleAssign(id) {
            this.loading = true
            let roleIds = await this.api.roles(id)
            this.assignForm = {
                id: id,
                roleIds: roleIds,
            }
            this.assignShow = true
            this.loading = false
        },
        async submitHandler(val) {
            this.submit(val, 'id').then(data => {
                this.editShow = false
                this.loadData()
            })
        }
    },
    computed: {
        headerMapping() {
            return {
                username: '用户名',
                realName: '用户姓名',
                phone: '手机',
                email: '邮箱',
                status: {
                    label: '状态',
                    dataMappingRev: this.$mapping.status
                },
                updateTime: '更新时间'
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
.nb-user-container {
    .button-cell {
        display: flex;
        justify-content: flex-start;
        .channel-select-button {
            margin-left: 10px;
        }
    }
}
</style>
