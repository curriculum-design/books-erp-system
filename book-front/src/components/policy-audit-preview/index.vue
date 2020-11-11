<template lang="pug">
.policy-audit-preview.margin-bottom-10
    p.flex-between
        span 政策操作
    el-table(:data="policyChangeList" border, :row-class-name="rowClassNameRender")
        el-table-column(label="是否长期")
            span(slot-scope="{row}") {{row.isLongTerm ? '是' : ''}}
        el-table-column(label="渠道名称")
            span(slot-scope="{row: {channel}}") {{channel.channelname}}
        el-table-column(label="渠道ID")
            span(slot-scope="{row}") {{row.channelId}}
        el-table-column(label="政策名称")
            span(slot-scope="{row: {rewardPolicy}}") {{rewardPolicy.name}}
        el-table-column(label="政策类型", prop="type")
            span(slot-scope="{row}") {{systemMapping.rewardPolicyType[row.rewardPolicy.type]}}
        el-table-column(label="发放方式", prop="type")
            span(slot-scope="{row}") {{systemMapping.rewardType[row.rewardPolicy.rewardType]}}
        el-table-column(label="绑定渠道" width="120")
            template(slot-scope="{row}")
                span {{row.settlementChannelName}}
        el-table-column(label="时间范围" width="280")
            template(slot-scope="{row}")
                span {{row.beginTime | date-format}} - {{row.endTime | date-format}}
        el-table-column(label="来自渠道申请", v-if="showChannelRequest")
            span(slot-scope="{row}") {{row.fromChannel}}
        el-table-column(label="本次操作")
            template(slot-scope="{row}")
                OperatorContent(:policy="row", :renderContent="renderOperator")
        el-table-column(label="操作" v-if="!readonly")
            template(slot-scope="{row}")
                .icon-btn.success(@click="cancelChange(row)") 撤销改动
</template>

<script>
import {mapGetters} from 'vuex'
import PolicyNameMix from '@/utils/mixins/policy-parse-mix'
export default {
    mixins: [PolicyNameMix],
    props: ['policyChannelList', 'readonly', 'showChannelRequest'],
    components: {
        OperatorContent: {
            props: {
                policy: {},
                renderContent: Function,
            },
            render(h) {
                const parent = this.$parent
                const {policy, renderContent, applyAuditData} = this
                return renderContent ? renderContent.call(parent._renderProxy, h, {policy, applyAuditData})
                : <span></span>
            }
        }
    },
    data() {
        return {
            hiddenDelete: false,
            canClearable: false
        }
    },
    methods: {
        cancelChange(row) {
            this.$emit('cancel-change', row)
        },
        rowClassNameRender({row}) {
            const renderData = this.getRenderData(row)
            return 'policy-audit-preview-table-row ' + (renderData.style || '')
        },
        getRenderData(row) {
            const CHANGE_VALUE = row[this.CHANGE_KEY]
            let text = '无改动'
            let style
            if (CHANGE_VALUE === this.CHANGE_KEY_REMOVE) {
                text = '移除'
                style = 'text-danger'
            } else if (CHANGE_VALUE === this.CHANGE_KEY_ADD) {
                text = '添加'
                style = 'text-success'
            } else if (CHANGE_VALUE === this.CHANGE_KEY_UPDATE) {
                text = '修改'
                style = ''
            }
            return {
                text,
                style
            }
        },
        renderOperator(h, {policy}) {
            const renderData = this.getRenderData(policy)
            return (
                <span class={renderData.style}>{renderData.text}</span>
            )
        }
    },
    computed: {
        ...mapGetters(['systemMapping']),
        policyChangeList() {
            const changeList = this.policyChannelList.filter(p => p[this.CHANGE_KEY])
            return changeList
        }
    }
}
</script>

<style lang="less">
.policy-audit-preview-table-row{
    &.text-danger{
        text-decoration:line-through;
        background: rgba(255, 183, 183, 0.28);
    }
    &.text-success{
        background: rgba(155, 255, 199, 0.2);
    }
}
</style>
