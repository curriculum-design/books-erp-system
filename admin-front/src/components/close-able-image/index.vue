<template lang="pug">
.close-able-image
  img(:src="src")
  i.fa.fa-times-circle-o(@click.stop="$emit('close')")
</template>

<script>
export default {
    props: ['src'],
}
</script>
<style lang="less" rel="stylesheet/less">
.close-able-image{
    position: relative;
    width: 100%;
    height: 100%;
    i.fa-times-circle-o{
        position: absolute;
        top: -8px;
        right: -8px;
        cursor: pointer;
        font-size: 1.5em;
        color: @danger-color;
    }
    img{
        max-height: 100%;
    }
}
</style>
