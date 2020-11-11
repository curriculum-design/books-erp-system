<template lang="pug">
el-dialog(title="编辑", :visible.sync="dialogShow", :close-on-click-modal="false", :append-to-body="true")
    el-form(v-model="form" @submit.prevent.native="submitHandler()" label-width="80px")
        el-form-item(label="用户名")
            el-input(v-model="form.username")
        el-form-item(label="用户姓名")
            el-input(v-model="form.realName")
        el-form-item(label="手机号")
            el-input(v-model="form.phone")
        el-form-item(label="邮箱")
            el-input(v-model="form.email")
        el-form-item(label="密码")
            el-input(v-model="form.password")
        el-form-item(label="状态")
            el-radio-group(v-model="form.status")
                el-radio(v-for="type, name in $mapping.status", :label="type", :key="type") {{name}}
        el-form-item.form-action.margin-top-20
            el-button(type="primary", @click="dialogShow = false") 取消
            el-button(type="success" native-type="submit") 保存
</template>

<script>
import formDialogMix from '@/utils/mixins/form-dialog-mix'
export default {
    mixins: [formDialogMix],
    created() {
        this.form.isEditFlag = !!this.form.username
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
