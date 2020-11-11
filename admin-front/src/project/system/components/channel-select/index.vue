<template lang="pug">
div
    .popover-mask(v-if="showPopper" @click="showPopper = false")
    .el-input.channel-popover-conatiner(v-loading="channelLoading")
        template(v-if="readonly")
            el-input(v-model="inputValue" readonly :disabled="disabled")
        template(v-else)
            el-input(readonly v-model="inputValue" @click.native="inputClickHander" placeholder="请选择渠道" clearable :disabled="disabled")
            .channel-select-popover-container(v-if="showPopper")
                el-select(v-model="selectChannelId" filterable remote placeholder="输入渠道名称搜索", :remote-method="channelFilter")
                    el-option(v-for="item in options", :key="item.channelid", :label="item.channelname", :value="item")
                el-tree.margin-top-20(:data="channels", :load="loadChannelNode" lazy, :props="channelTreeProps", :render-content="renderContent")
</template>

<script>
import ChannelDataMix from '../../mixins/channel-data-mix'
export default {
    mixins: [ChannelDataMix],
    props: ['value', 'readonly', 'disabled'],
    data() {
        return {
            showPopper: false,
            selectChannelId: '',
            options: [],
            channelLoading: false,
            cache: {}
        }
    },
    watch: {
        showPopper(val) {
            if (!val) {
                this.selectChannelId = ''
            }
        },
        selectChannelId(val) {
            if (val) {
                this.selectChannelIdHandler(val.channelid)
                this.$emit('select-channel', val)
            }
        },
    },
    computed: {
        inputValue: {
            get() {
                if (this.value) {
                    let channelname = this.cache[this.value]
                    if (channelname === undefined) {
                        this.getById(this.value)
                    }
                    return channelname + ' - ' + this.value
                }
                return this.value
            },
            set(val) {
                this.selectChannelIdHandler(val)
                try {
                    if (val === '') {
                        this.$emit('select-channel', val)
                    }
                } catch (e) {
                    console.log(e)
                }
            }
        }
    },
    methods: {
        async getById(channelid) {
            this.channelLoading = true
            try {
                const channel = await this.$api.bsChannel.get(channelid)
                this.setCache(channel)
            } finally {
                this.channelLoading = false
            }
        },
        setCache({channelid, channelname}) {
            this.$set(this.cache, channelid, channelname)
        },
        inputClickHander({target: {className}}) {
            if (className.indexOf('el-input__icon') === -1) {
                this.showPopper = true
            }
        },
        channelFilter(key) {
            this.$api.bsChannel.list({channelname: key})
            .then((data) => {
                this.options = data.data
                this.options.forEach((channel) => {
                    this.setCache(channel)
                })
            })
        },
        selectChannel(data, e) {
            e.stopPropagation()
            this.setCache(data)
            this.selectChannelIdHandler(data.channelid)
            this.$emit('select-channel', data)
        },
        selectChannelIdHandler(id) {
            this.$emit('input', id)
            this.$emit('change', id)
            this.showPopper = false
        },
        renderContent(h, {data, node, store}) {
            // <el-button disabled={data.children.length > 0} size="mini" type="danger" on-click={ () => this.remove(store, data) }>删除</el-button>
            return (
                <span class="">
                    <span>
                        <el-button size="mini" on-click={ (e) => this.selectChannel(data, e) }>选择</el-button>
                        <span> {data.channelname || `${data.channelid}(未命名)`} - {data.channelid}</span>
                    </span>
                </span>
            )
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.channel-select-popover-container{
    width: 375px;
    max-width: 90vw;
    height: 40vh;
    overflow: auto;
    position: absolute;
    z-index: 2000;
    background: #fff;
    padding: @global-padding;
    border: 1px solid #d3d3d3;
    transform: translate(-50%);
    left: 50%;
}
.channel-popover-conatiner{
    position: relative;
}
.popover-mask{
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    z-index: 1000;
}
</style>
