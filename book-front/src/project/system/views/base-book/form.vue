<template lang="pug">
el-dialog(title="编辑", :visible.sync="dialogShow", :close-on-click-modal="false", :append-to-body="true" width="480px")
    el-form(v-model="form" @submit.prevent.native="submitHandler()" label-width="80px")
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
        el-form-item.form-action.margin-top-20
            el-button(type="primary", @click="dialogShow = false") 取消
            el-button(type="success" native-type="submit") 保存
</template>

<script>
import formDialogMix from '@/utils/mixins/form-dialog-mix'
import {mapGetters} from 'vuex'
export default {
    props: ['publishers', 'bookTypes'],
    mixins: [formDialogMix],
    computed: {
        ...mapGetters['systemMapping']
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
