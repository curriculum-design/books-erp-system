<template lang="pug">
    el-transfer.policy-transfer(
    ref='transfer',
    v-loading="loading",
    v-model="curryIds",
    :data="curryList",
    :filter-method="filterMethod",
    :titles="['奖励/活动 (未选)', '奖励/活动 (已选)']",
    filterable,
    @change="checkList",
    @left-check-change="stopMoreAction")
        .option-label-style(slot-scope="{option}")
            span.policy-text {{option.label}}
            .long-term-select
                el-date-picker.time-picker-transfer(
                v-model='policyTime[option.key]',
                type='daterange',
                value-format='timestamp',
                start-placeholder='开始日期',
                end-placeholder='长期',
                :disabled="option.disabled || isAction(option.type)")
                p(v-if='option.type === 1')
                    span 申请为长期政策
                    el-select.select-width(v-model="longTermMapping[option.key]", :disabled="option.disabled")
                        el-option(label="是" :value="true")
                        el-option(label="否" :value="false")
</template>
<script>
    export default {
        props: {
            policyMonth: {
                type: Date
            },
            policyIds: {
                type: Array,
                default: []
            },
            list: {
                type: Array,
                default: []
            }
        },
        data() {
            return {
                longTermMapping: {},
                policyTime: {},
                curryIds: [],
                curryList: [],
                loading: false,
            }
        },
        created() {
            this.initData()
        },
        computed: {
            allData() {
                return {
                    longTermMapping: this.longTermMapping,
                    policyTime: this.policyTime,
                    policyIds: this.policyIds
                }
            }
        },
        methods: {
            initData() {
                this.loading = true
                this.longTermMapping = {}
                this.policyTime = {}
                this.curryIds = this.policyIds
                this.curryList = this.list
                const actionGroupById = {}
                this.curryList.map(policy => {
                    if (policy.type === 2 && policy.group === '默认') {
                        policy.type = 999
                    }
                    if (policy.type === 2 && this.curryIds.indexOf(policy.key) > -1) {
                        actionGroupById[policy.group] = {
                            id: policy.key
                        }
                    }
                })
                this.curryList = this.curryList.map((item) => {
                    this.setObjVal(this.policyTime, item.key, (item.time[0] ? item.time : []))
                    this.setObjVal(this.longTermMapping, item.key, item.isLongTerm || false)
                    if (item.type === 2 && actionGroupById[item.group]) {
                        if (actionGroupById[item.group].id !== item.key) {
                            item.disabled = true
                        }
                    }
                    return item
                })
                this.loading = false
            },
            filterMethod(query, item) {
                if (item && item.label && query) {
                    return item.label.indexOf(query) > -1
                }
                return true
            },
            isAction(type) {
                return [2, 999].indexOf(type) > -1
            },
            setObjVal(obj, key, val) {
                this.$set(obj, key, val)
            },
            deleteObjVal(obj, key) {
                this.$delete(obj, key)
            },
            stopMoreAction(arr, key) { // tips: 我也不知道以下代码是怎么运行成功的，慎重修改！！！
                // 能被选择的是不被禁用且不在已选的政策
                const canBeSelect = this.curryList.filter(({disabled, key}) => !disabled && this.curryIds.indexOf(key) === -1).length
                const actionIds = this.curryList.map(({type, key}) => {
                    if (type === 2) {
                        return key
                    }
                }).filter(i => i)
                if (key.indexOf(undefined) > -1) {
                    let ids = key.filter(id => id)
                    ids = ids.filter(id => actionIds.indexOf(id) > -1)
                    this.$refs.transfer.$children[0].checked =
                        this.$refs.transfer.$children[0].checked.concat(key).filter(id => ids.indexOf(id) === -1 || id !== undefined)
                    if (this.$refs.transfer.$children[0].checked.length > 0 && ids.length > 0) {
                        this.$refs.transfer.$children[0].checked = []
                        this.curryList.map(item => {
                            item.disabled = false
                        })
                    }
                    return
                }
                if (arr.length === canBeSelect) { // 全选
                    if (arr.length === key.length) { // 选中
                        this.$refs.transfer.$children[0].checked = this.curryList.map(item => {
                            if (item.type !== 2) { // 选择不是活动的政策
                                return item.key
                            }
                        })
                    } else {
                        if ([...new Set(this.$refs.transfer.$children[0].checked.concat(arr))].length === this.$refs.transfer.$children[0].checked.length) {
                            return
                        }
                        this.$refs.transfer.$children[0].checked.filter(id => {
                            return actionIds.indexOf(id) > -1
                        }).map(id => {
                            const groupItem = this.curryList.find(item => item.key === id)
                            if (groupItem && groupItem.type === 2) {
                                const {group} = groupItem
                                this.curryList.map(item => {
                                    if (item.group === group && item.key !== groupItem.id) { // 开启其他同组活动
                                        item.disabled = false
                                    }
                                })
                            }
                        })
                        this.$refs.transfer.$children[0].checked = []
                    }
                    return
                }
                const keyId = key[0]
                if (arr.indexOf(keyId) === -1) {
                    const groupItem = this.curryList.find(({key}) => key === keyId)
                    if (groupItem && groupItem.type === 2) {
                        const {group} = groupItem
                        this.curryList.map(item => {
                            if (item.group === group && item.key !== groupItem.key) { // 开启其他同组活动
                                item.disabled = false
                            }
                        })
                    }
                    return
                }
                arr.map(id => {
                    const groupItem = this.curryList.find(({key}) => key === id)
                    if (groupItem && groupItem.type === 2) {
                        const {group} = groupItem
                        this.curryList.map(item => {
                            if (item.group === group && item.key !== groupItem.key) { // 禁用其他同组活动
                                item.disabled = true
                            }
                        })
                    }
                })
            },
            checkList(data, direction, key) {
                if (direction === 'left') {
                    key.map(id => {
                        const groupItem = this.curryList.find(item => item.key === id)
                        if (groupItem && groupItem.type === 2) {
                            const {group} = groupItem
                            this.curryList.map(item => {
                                if (item.group === group && item.key !== groupItem.id) { // 开启其他同组活动
                                    item.disabled = false
                                }
                            })
                        }
                    })
                }
                this.$emit('update:policyIds', this.curryIds)
            }
        },
        watch: {
            allData: {
                deep: true,
                handler(val) {
                    this.$emit('change', val)
                }
            },
            list() {
                this.initData()
            }
        }
    }
</script>

<style lang="less">
    .policy-transfer{
        .el-transfer-panel {
            width: 800px !important;
            &:first-child {
                width: 330px!important;
            }
            .option-label-style {
                display: flex;
                justify-content: space-between;
                margin-right: 10px;
                .policy-text {
                    width: 330px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }
                .long-term-select {
                    display: flex;
                    justify-content: flex-start;
                    .select-width {
                        width: 70px;
                        margin-left: 8px;
                    }
                }
            }
        }
        .time-picker-transfer{
            width: 205px!important;
            margin-right: 10px!important;
        }
        .el-transfer-panel:nth-child(1) {
            .option-label-style {
                .long-term-select {
                    display: none;
                }
            }
        }
    }
</style>
