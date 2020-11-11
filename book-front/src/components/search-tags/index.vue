<template lang="pug">
    .search-tags 搜索条件:
        el-tag(v-for="value,key in filter", :key="key", type="",
        :closable="!readonly" @close="$emit('close', key)" v-if="value") {{valueFormat(value, key, format)}}
</template>

<script>
export default {
    props: ['filter', 'format', 'readonly'],
    created() {
        if (this.readonly === undefined) {
            this.readonly = false
        }
    },
    methods: {
        valueFormat(value, key, format = {}) {
            format = format[key]
            if (format) {
                let formatRev = {}
                Object.keys(format).forEach(k => {
                    formatRev[format[k]] = k
                })
                if (typeof format === 'function') {
                    value = format(value)
                } else {
                    value = format[value] || formatRev[value] || value
                }
            }
            return value
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
