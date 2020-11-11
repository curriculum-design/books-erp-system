<template lang="pug">
  .node-item(:style="{'padding-left': leftPadding + 'px'}")
      span.table-tree-node
          i(:class="currentClass") &nbsp;
      slot
</template>

<script>
export default {
    props: ['node'],
    computed: {
        currentClass() {
            const {node} = this
            if (node.loading) {
                return 'el-icon-loading'
            }
            if (node.isLoaded && node.children.length === 0) {
                return 'el-icon-document'
            }
            if (node.open) {
                return 'el-icon-caret-bottom'
            } else {
                return 'el-icon-caret-right'
            }
        },
        leftPadding() {
            const parents = this.node.parents || []
            return parents.length * 25
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.node-item{
    cursor: pointer;
    .table-tree-node{
        position: relative;
        display: inline-block;
        padding: 2px 0;
    }
}
</style>
