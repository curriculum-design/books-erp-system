<template lang="pug">
  .page-no-permission-container 您没有权限访问该页面...
</template>

<script>
export default {
    created() {
        // window.setTimeout(() => {
        //     this.$router.push(this.url)
        // }, 5000)
    },
    computed: {
        url() {
            return this.$route.query.redirect
        },
        name() {
            return this.$route.query.name
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
    .page-no-permission-container {}
</style>
