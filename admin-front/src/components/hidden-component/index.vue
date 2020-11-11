<template lang="pug">
  .hidden-component(:class="isShow ? 'shwidth' : ''")
      .full-height.scroll-able(v-show="isShow")
          slot
      .icon-show(@click="isShow = !isShow")
          template(v-if="isShow")
            i.el-icon-caret-left
          template(v-else)
            i.el-icon-caret-right
</template>

<script>
export default {
    props: ['value', 'noShow'],
    data() {
        return {
            privateShow: true,
        }
    },
    created() {
        if (this.noShow) {
            this.isShow = false
        }
    },
    computed: {
        isShow: {
            get() {
                if (this.value !== undefined) {
                    return this.value
                }
                return this.privateShow
            },
            set(val) {
                this.$emit('input', val)
                this.privateShow = val
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
  .hidden-component{
      position: relative;
      height: 100%;
      min-width: 15px;
      .icon-show{
          cursor: pointer;
          position: absolute;
          top: 45%;
          right: 0px;
          width: 10px;
          height: 30px;
          padding: 5px 10px;
          border-radius: 5px;
          &>i{
              font-size: 20px;
          }
      }
  }
  .shwidth{
      width: 210px;
  }
</style>
