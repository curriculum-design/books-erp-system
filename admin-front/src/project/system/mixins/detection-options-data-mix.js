export default {
    data() {
        return {
            treeData: [],
            loadingTree: false,
        }
    },
    computed: {
        parseTreeData() {
            let childList = []
            if (this.treeData.length !== 0) {
                this.treeData.map(item => {
                    if (item.shopPropertyValues) {
                        childList = [...childList, ...item.shopPropertyValues]
                    }
                })
                let parentList = this.treeData.map(item => {
                    let p = {
                        id: item.id,
                        label: item.name,
                        ...item,
                        children: []
                    }
                    return p
                })
                parentList.map(p => {
                    childList.map(c => {
                        if (c.propertyNameId === p.id) {
                            let ps = {
                                id: c.id,
                                label: c.value,
                                ...c,
                                children: []
                            }
                            p.children.push(ps)
                        }
                    })
                })
                return parentList
            }
        }
    },
    // async created() {
    //     await this.getTreeData()
    // },
    methods: {
        async getTreeData() {
            this.loadingTree = true
            this.treeData = await this.$api.shopPropertyName.findWithValues()
            this.loadingTree = false
            // this.loadTreeDataSuccess && this.loadTreeDataSuccess()
        }
    }
}
