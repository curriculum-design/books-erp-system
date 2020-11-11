<template lang="pug">
  el-dialog.take-photo-dialog(:visible.sync="dialogShow" title="编辑照片", :modal="false", :append-to-body="true", width="840px", top="0")
      DrawingBoard(:background="image", width="800", height="600", @change="drawChange")
      .text-right
          el-button(@click="dialogShow = false") 关闭
          el-button(@click="saveImage()" type="primary") 保存
</template>

<script>
import DialogMix from '@/utils/mixins/dialog-mix'
import DrawingBoard from '@/components/drawing-board'
export default {
    props: ['image'],
    mixins: [DialogMix],
    components: {DrawingBoard},
    data() {
        return {
            drawData: '',
        }
    },
    methods: {
        drawChange(data) {
            this.drawData = data
        },
        saveImage() {
            if (this.drawData) {
                this.$emit('save', this.drawData)
            }
            this.dialogShow = false
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
