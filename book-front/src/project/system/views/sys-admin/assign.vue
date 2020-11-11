<template lang="pug">
  el-dialog(title="分配角色", :visible.sync="dialogShow" v-loading="loading")
      el-transfer(v-model="form.roleIds", :data="roles" filterable, :filter-method="filterMethod" filter-placeholder="搜索角色", :button-texts="['移除', '添加']", :titles="['未有角色', '已有角色']")
      .form-action.margin-top-20
          el-button(type="primary", @click="dialogShow = false") 取消
          el-button(type="success" @click="submitHanlder()") 保存
</template>

<script>
import DialogMix from '@/utils/mixins/dialog-mix'
export default {
    mixins: [DialogMix],
    props: ['value'],
    async created() {
        let roles = await this.$nbRoleService.all()
        this.roles = roles.map(r => {
            return {
                label: r.name,
                key: r.id
            }
        })
    },
    computed: {
        form: {
            get() {
                return this.value
            },
            set(val) {
                this.$emit('input', val)
            }
        },
    },
    data() {
        return {
            loading: false,
            roles: [],
        }
    },
    methods: {
        async submitHanlder() {
            this.loading = true
            try {
                await this.$tAdminService.assign(this.form)
                this.dialogShow = false
            } finally {
                this.loading = false
            }
        },
        filterMethod(query, item) {
            if (item && item.label && query) {
                return item.label.indexOf(query) > -1
            }
            return true
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
