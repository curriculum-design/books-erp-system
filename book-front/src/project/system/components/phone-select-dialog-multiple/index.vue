<template lang="pug">
  ElDialog.phone-select-dialog(:visible.sync="dialogShow", title="选择手机" :fullscreen="true", :append-to-body="true", top="0")
      PhoneSelect(@select="selectHandler", :selectedData="this.value" ref="phoneSelect")
      .margin-top-10.text-right
          el-button(@click="dialogShow = false") 取消
          el-button(type="primary" @click="yes") 确定
</template>

<script>
import DialogMix from '@/utils/mixins/dialog-mix'
import PhoneSelect from '../phone-select-multiple'
export default {
    props: ['value', 'single'],
    mixins: [DialogMix],
    components: {PhoneSelect},
    data() {
        return {
            selectData: []
        }
    },
    watch: {
        dialogShow(val) {
            if (this.single && !val) {
                this.$refs.phoneSelect.reset()
            }
        }
    },
    methods: {
        yes() {
            this.$emit('select', this.selectData)
            this.dialogShow = false
        },
        selectHandler(data) {
            // console.log('selectData', this.value)
            this.selectData = data
            if (this.single) {
                if (data.length) {
                    this.selectData = data[0]
                    this.yes()
                    this.selectData = []
                } else {
                    this.selectData = []
                }
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
