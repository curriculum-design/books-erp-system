<template lang="pug">
  .text-edit-container
      .input-container
          input(v-model="text", :style="{fontSize: fontSize + 'px', color: color}", placeholder="请输入内容")
      .save-container
          button(@click="close") 关闭
          button(@click="yes") 确定
</template>

<script>
export default {
    props: ['fontSize', 'color'],
    data() {
        return {
            text: ''
        }
    },
    methods: {
        close() {
            this.$emit('close')
        },
        yes() {
            this.$emit('yes', this.text)
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.text-edit-container{
    input{
        background: none;
        border: 1px solid #eee;
        font-weight: bold;
        padding: 10px;
    }
    button{
        background: rgba(255, 255, 255, 0.2);
        border: 1px solid #eee;
        border-radius: 2px;
        padding: 5px;
        color: #000;
        margin-right: 10px;
    }
}
</style>
