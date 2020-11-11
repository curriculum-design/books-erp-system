<template lang="pug">
  ElDialog.image-preview-dialog(:visible.sync="dialogShow", v-if="dialogShow"  width="90%" top="5vh")
    .image-container
        img(:src="src" @click="dialogShow = false")
</template>

<script>
import DialogMix from '@/utils/mixins/dialog-mix'
import {Dialog} from 'element-ui'
export default {
    mixins: [DialogMix],
    props: ['src', 'size'],
    components: {ElDialog: Dialog},
}
</script>
<style lang="less" rel="stylesheet/less">
.image-preview-dialog {
    z-index: 9999 !important;
}
</style>
