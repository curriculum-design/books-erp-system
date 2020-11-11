<template lang="pug">
  .version-footer
      span Copyright © 2018 ??? Inc@v{{version}}
</template>

<script>
import packageJson from '../../../package.json'
export default {
    computed: {
        version() {
            return packageJson.version
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
  .version-footer{
      text-align: center;
      margin-top: @global-padding;
      color: fadeout(#000, 50%);
  }
</style>
