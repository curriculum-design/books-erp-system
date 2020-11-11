<template lang="pug">
.channel-select-multiple-container(:class="{readonly: readonly}", :style="{width: width}")
    el-tag(v-for="channel in hasChannelInfo",
    @close="deleteChannel(channel.id)",
    :key="channel.id", :closable="!readonly", type="primary") {{channel.id}}: {{channel.name}}
</template>

<script>
export default {
    props: {
        hasChannelInfo: {
            default() {
                return []
            }
        },
        readonly: {
            default: false
        },
        width: String,
    },
    methods: {
        deleteChannel(id) {
            this.$emit('deleteChannel', id)
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.channel-select-multiple-container{
    min-height: 20vh;
    .el-tag__close{
        float: right;
    }
    &.readonly{
        cursor: pointer;
        &:hover{
            border-color: #c0c4cc;
        }
        width: 160px;
        min-height: 28px;
        border-radius: 4px;
        border: 1px solid #dcdfe6;
        max-height: 120px;
        background: #fff;
        &>.el-tag{
            margin-bottom: 2px;
            width: auto;
            margin-right: 5px;
        }
        padding: 5px;
        max-width: 500px;
        .full-width & {
            width: 100%;
            max-width: unset;
        }
    }
    overflow: auto;
    border: 1px solid #eee;
    padding: @global-padding;
    &>.el-tag{
        margin-right: 1%;
        width: 49%;
        margin-bottom: 10px;
        overflow: hidden;
    }
}
</style>
