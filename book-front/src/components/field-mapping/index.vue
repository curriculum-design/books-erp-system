<template lang="pug">
el-table-column(label="渠道")
    span(slot-scope="scope") {{getValue(scope.row)}}
</template>

<script>
import TableMix from '@/utils/mixins/table-mix'
export default {
    mixins: [TableMix],
    props: ['headerMapping', 'prop'],
    methods: {
        getValue(data) {
            let key = this.prop
            return this.fieldParse(this.headerMapping[key], data[key], data)
        },
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
