<template lang="pug">
    .content-body
        .title(v-if="title || $slots.title")
            slot(name="title") {{title}}
        .filter.gray-section(v-if="$slots.filter")
            slot(name="filter")
        .content
            slot
        .footer.gray-section(v-if="$slots.footer")
            slot(name="footer")
</template>

<script>
export default {
    props: ['title']
}
</script>
<style lang="less" rel="stylesheet/less">
.content-body{
    .title{
        margin-bottom: 10px;
    }
    .filter{
        .el-form--inline{
            &.no-margin-bottom-form{
                .el-form-item{
                    margin-bottom: 0px;
                }
            }
            .el-form-item{
                margin-bottom: 10px;
            }
        }
    }
    .content{
        margin-bottom: @global-padding;
    }
    .footer{
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>
