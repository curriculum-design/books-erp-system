<template lang="pug">
  .page-no-found-container {{name}}({{url}}) 页面正在构建中...
</template>

<script>
export default {
    computed: {
        url() {
            return this.$route.query.url
        },
        name() {
            return this.$route.query.name
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
