export default {
    data() {
        return {
            historyTreeOpenData: {}
        }
    },
    methods: {
        autoOpenTree(dom) {
            this.$nextTick(() => {
                this.openChildNodes(dom.root.childNodes)
            })
        },
        openChildNodes(childNodes) {
            childNodes.forEach(node => {
                let open = this.historyTreeOpenData[node.data.id]
                if (open) {
                    node.expanded = open
                }
                if (node.childNodes.length) {
                    this.openChildNodes(node.childNodes)
                }
            })
        },
        rememberOpen(node) {
            this.historyTreeOpenData[node.data.id] = node.expanded
        }
    }
}
