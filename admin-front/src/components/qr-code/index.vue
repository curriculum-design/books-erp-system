<template lang="pug">
  img(:src="src")
</template>

<script>
import jrQrcode from 'jr-qrcode'
export default {
    props: ['url', 'level'],
    computed: {
        src() {
            let base64 = jrQrcode.getQrBase64(this.url, {
                correctLevel: this.level || 1,
                foreground: '#000'
            })
            return base64
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
