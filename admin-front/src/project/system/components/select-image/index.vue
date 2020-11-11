<template lang="pug">
  .select-image-container
      CloseAbleImage(:src="value" v-if="value" @close="closeImage('')" @click.native="imageClickHandler")
      .select-image(v-if="!value" @click="selectImage")
          div
              i.fa.fa-plus
              p 请选择图片
</template>

<script>
export default {
    props: ['value', 'noPreview'],
    methods: {
        selectImage() {
            this.$selectImage().then(img => {
                this.changeImage(img.url)
            })
        },
        closeImage() {
            this.$confirm('你确定要删除吗?')
            .then(() => {
                this.changeImage('')
            })
        },
        changeImage(image) {
            this.$emit('input', image)
            this.$emit('change', image)
        },
        imageClickHandler() {
            if (this.noPreview) {
                this.$emit('image-click')
            } else {
                this.$imagePreview(this.value)
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.select-image-container{
    &+&{
        margin-left: @global-padding;
    }
    @size:148px;
    height: @size;
    width: @size;
    background: #eee;
    cursor: pointer;
    .select-image{
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
    }
    i.fa-plus{
        font-size: 40px;
    }
    .close-able-image{
        display: flex;
        align-items: center;
    }
}
</style>
