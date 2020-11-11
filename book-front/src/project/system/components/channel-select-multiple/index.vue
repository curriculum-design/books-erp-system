<template lang="pug">
div
  template(v-if="type === 'button'")
      el-button(@click="clickHandler")
          slot
  template(v-else)
      ChannelSelectInfo(:hasChannelInfo="realChannelInfo" @deleteChannel="deleteChannel" @click.native="clickHandler" readonly, :width="width")
  el-dialog(title="选择渠道", :visible.sync="dialogShow", :append-to-body="true", top="2%")
      ChannelSelectInfo(:hasChannelInfo="hasChannelInfo" @deleteChannel="deleteChannel")
      el-row.margin-top-20(:gutter="10")
          el-col(:span="8")
              el-select(v-model="selectChannelId" filterable remote placeholder="输入名称进行添加", :remote-method="channelFilter")
                  el-option(v-for="item in options", :key="item.channelid", :label="item.channelid + ':' + item.channelname", :value="item.channelid")
          el-col(:span="8")
              el-select(v-model="selectChannelId" filterable remote placeholder="输入渠道ID添加", :remote-method="channelFilterByChannelId")
                  el-option(v-for="item in options", :key="item.channelid", :label="item.channelid + ':' + item.channelname", :value="item.channelid")
          el-col(:span="8")
              el-button(type="primary" @click="clearHasHandler") 清空已选({{hasChannel.length}})
              el-button(@click="multipleSelectShow = true") 批量选择
      .tree-select-container.margin-top-20
          el-tree(ref="tree", :data="channels", :load="loadChannelNode" lazy, :render-content="renderContent")
      .form-action.margin-top-20
          el-button(type="primary", @click="dialogShow = false") 关闭
          el-button(type="primary", @click="submitHandler") 保存
  el-dialog(title="批量选择", :visible.sync="multipleSelectShow", :append-to-body="true")
      el-radio-group(v-model="multipleSelectModel")
        el-radio(:label="1") 渠道ID
        el-radio(:label="2") 渠道名称
      el-input(type="textarea" v-model="channelText")
      .form-action.margin-top-20
          el-button(@click="multipleSelectShow = false") 取消
          el-button(type="primary", @click="multipleSelect") 确定
</template>

<script>
import ChannelDataMix from '../../mixins/channel-data-mix'
import ChannelSelectInfo from './channel-select-info'
export default {
    mixins: [ChannelDataMix],
    components: {ChannelSelectInfo},
    props: ['value', 'type', 'width'],
    data() {
        return {
            dialogShow: false,
            selectChannelId: '',
            options: [],
            cache: {},
            hasChannel: [],
            channelText: '',
            multipleSelectShow: false,
            multipleSelectModel: 1,
        }
    },
    watch: {
        selectChannelId(val) {
            if (val) {
                this.addChannel(val)
                this.selectChannelId = ''
            }
        },
        value() {
            this.dataInit()
        }
    },
    async created() {
        this.dataInit()
    },
    computed: {
        realChannelInfo() {
            let channelids = this.value
            if (typeof this.value === 'string') {
                channelids = this.value.split(',')
            } else if (!channelids) {
                channelids = []
            }
            let noCacheItem = 0
            const channelInfos = channelids.filter(c => c).map(id => {
                let channel = {
                    id: id,
                    name: this.cache[id],
                }
                if (!channel.name) {
                    noCacheItem++
                }
                return channel
            })
            noCacheItem && this.dataInit()
            return channelInfos
        },
        hasChannelInfo() {
            let cache = this.cache
            let channelInfo = this.hasChannel.map(id => {
                let channel = {
                    id: id,
                    name: cache[id],
                }
                return channel
            })
            return channelInfo
        }
    },
    methods: {
        async multipleSelect() {
            let channelArray = this.channelText.replace(/[\r\n]/g, ' ').split(' ').filter(a => a)
            const {diffData, channelList} = await this.$capi.bsChannel.findByChannelIdOrName({
                data: channelArray,
                mode: this.multipleSelectModel // 1 渠道ID   2 渠道名称
            })
            this.$alert('添加成功!' + (diffData.length ? ('，部分数据未找到: ' + diffData.join(', ')) : ''))
            channelList.forEach((channel) => {
                this.setCache(channel)
                this.addChannel(channel.channelid)
            })
            this.multipleSelectShow = false
            this.channelText = ''
        },
        clearHasHandler() {
            this.$emit('input', null)
        },
        clickHandler() {
            this.$emit('click')
            this.dialogShow = true
        },
        submitHandler() {
            let value = this.hasChannel
            if (typeof this.value === 'string') {
                value = this.hasChannel.join(',')
            }
            this.$emit('input', value)
            this.$emit('submit', value)
            this.dialogShow = false
        },
        async dataInit() {
            this.hasChannel = this.value || []
            if (typeof this.hasChannel === 'string') {
                this.hasChannel = this.value.split(',')
            }
            if (this.hasChannel.length) {
                const channels = await this.$api.bsChannel.getByIds({ids: this.hasChannel})
                channels.forEach((channel) => {
                    this.setCache(channel)
                })
            }
        },
        isHasChannel(val) {
            return this.hasChannel.indexOf(val) >= 0
        },
        addChannel(val) {
            if (!this.isHasChannel(val)) {
                this.hasChannel = Array.from(new Set([val, ...this.hasChannel]))
            }
        },
        deleteChannel(val) {
            let index = this.hasChannel.indexOf(val)
            if (index > -1) {
                this.hasChannel.splice(index, 1)
            }
        },
        channelFilterByChannelId(key) {
            this.$api.bsChannel.list({channelid: key, pageSize: 30})
            .then((data) => {
                this.options = data.data
                this.options.forEach((channel) => {
                    this.setCache(channel)
                })
            })
        },
        channelFilter(key) {
            this.$api.bsChannel.list({channelname: key, pageSize: 30})
            .then((data) => {
                this.options = data.data
                this.options.forEach((channel) => {
                    this.setCache(channel)
                })
            })
        },
        setCache({channelid, channelname}) {
            this.$set(this.cache, channelid, channelname)
        },
        renderContent(h, {data, node, store}) {
            return (
                <span class="flex-between">
                    <span>{data.channelid}: {data.channelname}</span>
                    <span>
                        {!this.isHasChannel(data.channelid) && <el-button size="mini" on-click={ (e) => e.stopPropagation() || (this.setCache(data) || this.addChannel(data.channelid)) }>添加</el-button>}
                        {this.isHasChannel(data.channelid) && <el-button size="mini" type="danger" on-click={ (e) => e.stopPropagation() || this.deleteChannel(data.channelid) }>删除</el-button>}
                    </span>
                </span>
            )
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.tree-select-container{
    height: 40vh;
    overflow: auto;
}
</style>
