export default {
    data() {
        return {
            channels: [],
            channelMap: {},
            channelTreeProps: {
                label: 'channelname',
                children: 'children',
                isLeaf: 'isLeaf'
            },
            loadingTree: false,
        }
    },
    methods: {
        findDataById(id, channels, val) {
            let data
            channels.every(channel => {
                if (channel && channel.id === id) {
                    if (channel.pchannelid !== val.pchannelid) {
                        this.loadRootNode()
                    } else {
                        Object.keys(channel).forEach(k => {
                            this.$set(channel, k, val[k])
                        })
                    }
                    return false
                } else if (channel.children && channel.children.length > 1) {
                    data = this.findDataById(id, channel.children, val)
                    return false
                }
                return true
            })
            return data
        },
        editNode(id, val) {
            this.findDataById(id, this.channels, val)
        },
        async loadRootNode() {
            let channels = await this.$bsChannelService.getByParent(null, true)
            this.channels = this.dataToNode(channels)
        },
        async channelClickHandler(data, node) {
            if (node.data.loaded) {
                return
            }
            node.data.loaded = true
            node.data.children = []
            this.$nextTick(async () => {
                node.loading = true
                let {channelid} = data
                let channels = await this.$bsChannelService.getByParent(channelid)
                node.data.children = this.dataToNode(channels)
                node.loading = false
            })
        },
        async loadChannelNode(node, resolve) {
            this.loadingTree = true
            const data = node.data
            let channels = await this.$bsChannelService.getByParent(data.channelid)
            await resolve(channels)
            this.$nextTick(() => {
                this.afterTreeLoading(channels)
            })
            this.loadingTree = false
        },
        dataToNode(channels) {
            const channelsMap = {}
            channels = channels.map(channel => {
                let m = {
                    id: channel.channelid,
                    label: channel.channelname,
                    ...channel,
                    loaded: true,
                    children: [],
                }
                channelsMap[channel.channelid] = m
                this.$set(this.channelMap, channel.channelid, channel)
                return m
            })
            const root = []
            channels.forEach(m => {
                if (m.pchannelid && channelsMap[m.pchannelid]) {
                    channelsMap[m.pchannelid].children.push(m)
                } else {
                    root.push(m)
                }
            })
            channels.forEach(c => {
                if (!c.children.length && c.hasChildren) {
                    c.loaded = false
                    c.children = [{}]
                }
            })
            return root
        },
        beforeTreeLoading() {},
        afterTreeLoading() {},
    }
}
