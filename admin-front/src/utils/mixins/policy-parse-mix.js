import CommonUtils from '@/utils/CommonUtils'
export default {
    data() {
        return {
            CHANGE_KEY_UPDATE: 'CHANGE_KEY_UPDATE',
            CHANGE_KEY_REMOVE: 'CHANGE_KEY_REMOVE',
            CHANGE_KEY_ADD: 'CHANGE_KEY_ADD',
            CHANGE_KEY: 'CHANGE_KEY',
        }
    },
    methods: {
        policyNameParse({rate, multiLevel, reachReward}) {
            const policyNames = []
            if (rate !== 0) {
                policyNames.push(`百分比 ${((rate * 100).toFixed(2) || 0)} %`)
            }
            if (multiLevel != null && multiLevel.length > 0) {
                policyNames.push(multiLevel.split('|').map(level => {
                    const [money, reward] = level.split(':')
                    return `满 ${money} 奖 ${reward} `
                }).join(','))
            }
            if (reachReward) {
                policyNames.push(`达量限制 ${reachReward}`)
            }
            return policyNames.join('; ')
        },
        async processPolicyChannelChange(channelRewardPolicyList, findByIdsFunction, auditData, currentApply) {
            const {channelname: channelName} = currentApply
            const {
                changePolicyMap,
                newPolicyMap,
                removePolicyMap
            } = auditData
            const currentPolicyChannelList = []
            if (newPolicyMap) {
                channelRewardPolicyList.forEach(policyChannel => {
                    const updatePolicy = changePolicyMap[policyChannel.id]
                    const removePolicy = removePolicyMap[policyChannel.id]
                    const data = {...policyChannel}
                    if (updatePolicy) {
                        Object.keys(updatePolicy).forEach(key => {
                            this.$set(data, key, updatePolicy[key])
                        })
                        this.$set(data, this.CHANGE_KEY, this.CHANGE_KEY_UPDATE)
                    } else if (removePolicy) {
                        this.$set(data, this.CHANGE_KEY, this.CHANGE_KEY_REMOVE)
                    }
                    currentPolicyChannelList.push(data)
                })
                const policyIds = Object.values(newPolicyMap).map(policyChannel => policyChannel.policyId)
                if (policyIds.length) {
                    const policyList = await findByIdsFunction({ids: policyIds})
                    const policyMap = CommonUtils.arrayToObjectMap(policyList, 'id')
                    Object.values(newPolicyMap).forEach(policyChannel => {
                        currentPolicyChannelList.push({
                            ...policyChannel,
                            channel: {
                                channelname: channelName,
                            },
                            rewardPolicy: policyMap[policyChannel.policyId],
                            [this.CHANGE_KEY]: this.CHANGE_KEY_ADD
                        })
                    })
                }
                return currentPolicyChannelList
            }
            return channelRewardPolicyList
        },
    }
}
