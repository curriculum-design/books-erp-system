<template lang="pug">
el-table.mini-table(:data="treeData", ref="table" border, :max-height="maxHeight", :height="height" @selection-change="selectionChange")
    el-table-column(type="selection", width="55" v-if="selectionChange")
    el-table-column(:prop="key", :key="key", :label="value.label || value", v-for="value, key in headerMapping", :show-overflow-tooltip="true", :width="value.width")
        template(slot-scope="scope")
            TreeNode(v-if="value.isNode",
            :node="nodeMap[scope.row[idKey]]",
            @click.native="treeClick(scope)")
              NodeContent(:node="nodeMap[scope.row[idKey]]", :label="fieldParse(value, scope.row[key], scope.row)", :renderContent="value.renderContent")
            NodeContent(:node="nodeMap[scope.row[idKey]]", v-else :label="fieldParse(value, scope.row[key], scope.row)", :renderContent="value.renderContent")
    slot
</template>

<script>
import TreeNode from './tree-node'
import TableMix from '@/utils/mixins/table-mix'
export default {
    components: {
        TreeNode,
        NodeContent: {
            props: {
                label: {},
                node: {
                    require: true,
                },
                renderContent: Function,
            },
            render(h) {
                const parent = this.$parent
                const {node, renderContent, label} = this
                const {data} = node
                return renderContent ? renderContent.call(parent._renderProxy, h, {node, data, label})
                : <span>{this.label}</span>
            }
        }
    },
    mixins: [TableMix],
    props: {
        maxHeight: {},
        height: {},
        saveState: {},
        defaultExpand: {
            default: false,
        },
        headerMapping: {
            default() {
                return {}
            }
        },
        prop: {
            default() {
                return {
                    children: 'children',
                    id: 'id'
                }
            }
        },
        data: {
            default() {
                return []
            }
        },
        lazy: {default: false},
        load: {
            default: () => {
                return () => {}
            }
        },
        loadNode: Function,
        selectionChange: {
            default: () => {
                return 0
            }
        },
    },
    data() {
        return {
            nodeMap: {},
            nodeSort: 1,
            nodeArrays: [],
            stateMap: {},
        }
    },
    watch: {
        data: {
            deep: true,
            handler() {
                this.initNodeTree()
            }
        }
    },
    created() {
        this.initNodeTree()
    },
    computed: {
        idKey() {
            return this.prop.id
        },
        treeData() {
            const dataArray = []
            const showMaps = {}
            const sortNodes = (nodes) => {
                return nodes.sort((a, b) => a.sort - b.sort)
            }
            const isParentOpen = parent => {
                const parentId = parent.id
                if (parentId) {
                    const parentNode = this.nodeMap[parentId]
                    if (parentNode.show && parentNode.parent) {
                        return isParentOpen(parentNode.parent)
                    } else {
                        return parentNode.show
                    }
                } else {
                    return true
                }
            }
            const showNodes = (nodes) => {
                sortNodes(nodes)
                nodes.forEach(node => {
                    if (node.parents.length === 0 || isParentOpen(node.parent)) {
                        if (!showMaps[node.id]) {
                            showMaps[node.id] = node
                            dataArray.push(node.data)
                            if (node.children.length) {
                                showNodes(node.children.map(c => this.nodeMap[c[this.idKey]]))
                            }
                        }
                    }
                })
            }
            showNodes(Object.values(this.nodeMap))
            return dataArray
        }
    },
    methods: {
        showAllNodes() {
            Object.values(this.nodeMap).forEach(node => {
                node.show = true
                if (this.lazy) {
                    node.isLoaded = false
                } else {
                    node.open = true
                }
            })
        },
        treeClick({row, $index}) {
            const id = row[this.idKey]
            const node = this.nodeMap[id]
            if (node.open) {
                this.closeNode(node)
            } else {
                this.openNode(node)
            }
            this.stateMap = {...this.stateMap, [id]: node.open}
        },
        reloadNodeData(id) {
            return new Promise(async (resolve, reject) => {
                const node = this.nodeMap[id]
                if (this.loadNode) {
                    this.loadNode(id).then(data => {
                        node.data = data
                    })
                }
                if (node.isLoaded) {
                    node.isLoaded = false
                    await this.openNode(node)
                }
                resolve()
            })
        },
        async openNode(node) {
            if (this.lazy && !node.isLoaded) {
                node.loading = true
                const children = await this.load(node.data)
                node.loading = false
                node.isLoaded = true
                node.open = true
                node.show = true
                this.initNode(children, node)
            } else {
                node.isLoaded = true
                node.open = true
                node.show = true
            }
        },
        closeNode(node) {
            node.open = false
            node.show = false
        },
        getOrCreateNode(data, parents = [], parent) {
            const nodeId = data[this.idKey]
            const node = {
                open: false,
                children: [],
                data: data,
                sort: ((parent && parent.sort) || 1) * 10 + this.nodeSort++,
                parent,
                id: nodeId,
                loading: false,
                show: false,
                isLoaded: false,
                parents: [...parents],
            }
            if (!this.lazy) {
                node.isLoaded = true
            }
            this.$set(this.nodeMap, nodeId, node)
            return node
        },
        resetMap() {
            const dataMap = {}
            this.data.forEach(d => {
                const id = d[this.idKey]
                dataMap[id] = true
            })
            Object.keys(this.nodeMap).filter(id => !dataMap[id]).forEach(id => {
                this.$delete(this.nodeMap, id)
            })
        },
        initNodeTree() {
            this.resetMap()
            this.initNode(this.data)
            Object.keys(this.stateMap).forEach(k => {
                if (this.stateMap[k]) {
                    const node = this.nodeMap[k]
                    if (node) {
                        this.openNode(node)
                    }
                }
            })
            if (this.defaultExpand) {
                this.$nextTick(() => {
                    this.showAllNodes()
                })
            }
        },
        initNode(data = [], parentNode = {parents: []}) {
            data.forEach(d => {
                const parents = [...parentNode.parents]
                if (parentNode.data) {
                    parents.push(parentNode.data)
                }
                const node = this.getOrCreateNode(d, parents, parentNode)
                const children = d[this.prop.children]
                if (children && children.length) {
                    node.children = children
                    this.initNode(children, node)
                }
                parentNode.children = data
            })
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
