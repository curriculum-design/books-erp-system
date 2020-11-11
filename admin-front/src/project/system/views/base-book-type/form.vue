<template lang="pug">
el-dialog(title="编辑", :visible.sync="dialogShow", :close-on-click-modal="false", :append-to-body="true" width="400px")
    el-form(v-model="form" @submit.prevent.native="submitHandler()" label-width="80px")
        el-form-item(label="类型名称")
            el-input(v-model="form.name")
        el-form-item.form-action.margin-top-20
            el-button(type="primary", @click="dialogShow = false") 取消
            el-button(type="success" native-type="submit") 保存
</template>

<script>
import formDialogMix from '@/utils/mixins/form-dialog-mix'
import {mapGetters} from 'vuex'
export default {
    mixins: [formDialogMix],
    computed: {
        ...mapGetters['systemMapping']
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
