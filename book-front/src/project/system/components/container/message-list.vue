<template lang="pug">
  .message-container
     .message-title 站内消息通知
     .message-list(v-if="messages && messages.length")
         .message-item.click-able(v-for="msg in messages" @click="msgClickHandler")
             .text-primary {{msg.title}}
             .text-sencond {{msg.createtm | date-format}}
     .message-item(v-else) 暂无消息
     .message-item.click-able(@click="$router.push({name: 'nb-message'})")
         .text-primary 消息列表>>
</template>

<script>
import Types from '@/project/system/store/types'
import {mapGetters} from 'vuex'
export default {
    created() {
        this.startAutoLoadMessage()
    },
    methods: {
        msgClickHandler(msg) {
            if (msg.linkHref) {
                location.href = msg.linkHref
            } else {
                this.$router.push({name: 'nb-message', params: {id: msg.id}})
            }
        },
        startAutoLoadMessage() {
        },
        async loadMessage() {
            const lastMessage = this.totalMessage
            await this.$store.dispatch(Types.LOAD_UNREAD_MESSAGE)
            if (window.Notification) {
                Notification.requestPermission().then(() => {
                    const newMessageNumber = this.totalMessage - lastMessage
                    if (newMessageNumber > 0) {
                        const firstMessage = this.messages[0]
                        let title = '【闪回收】'
                        let message = `你有${newMessageNumber}条新消息，点击查看`
                        if (newMessageNumber === 1) {
                            title += firstMessage.title
                            message = firstMessage.message
                        }
                        const notify = new Notification(title, {
                            body: message,
                            icon: '//shanhs.oss-cn-shenzhen.aliyuncs.com/newboss/2017-11-01/5fc4a570-1b98-41bc-aea2-a000c86869bd.jpg'
                        })
                        notify.onclick = () => {
                            if (newMessageNumber === 1 && firstMessage.linkHref) {
                                this.$api.nbMessage.get(firstMessage.id)
                                location.href = firstMessage.linkHref
                            } else {
                                this.$router.push({name: 'nb-message'})
                            }
                            notify.close()
                        }
                    }
                })
            }
        },
    },
    computed: {
        totalMessage() {
            return this.unReadMessage.total || 0
        },
        messages() {
            return this.unReadMessage.data
        },
        ...mapGetters(['unReadMessage'])
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.message-container{
    .message-title{
        padding: 15px;
        font-size: 1.2em;
        background: #eaedf1;
    }
    .message-item{
        &.click-able{
            cursor: pointer;
            &:hover{
                background: rgba(195, 195, 195, 0.15);
            }
        }
        padding: 10px;
        line-height: 1.6;
        border-bottom: 1px solid #eee;
    }
}
.el-popover.message-popover{
    padding: 0;
}
</style>
