<template lang="pug">
  .boss-header
      div(style="width:80%")
          span(style="font-size:30px;margin-left:100px;padding-top:3px") 图书馆进销存管理系统
      .right-container
          el-popover(ref="messagePop"
          popper-class="message-popover"
          placement="bottom"
          width="300"
          trigger="hover")
              MessageList
          el-dropdown.header-item
              span.current-org
                  | {{user.username && `(${user.username})`}}
                  i.el-icon-caret-bottom.el-icon--right
              el-dropdown-menu(slot="dropdown")
                  el-dropdown-item(@click.native="showModifyPwd") 修改密码
          .header-item(@click="exit") 退出
</template>

<script>
import QrCode from '@/components/qr-code'
import Types from '@/project/system/store/types'
import DOMAIN from '@/utils/DOMAIN'
import {mapGetters} from 'vuex'
import MessageList from './message-list'
import LS from '@/utils/local-storage'
// import WebSocket from '../../../../utils/web-socket'
export default {
    components: {QrCode, MessageList},
    data() {
        return {
            systemParamShow: false,
        }
    },
    methods: {
        auditHandler() {
            this.$router.push('/dt-workbench')
        },
        exit() {
            this.$store.dispatch(Types.EXIT)
            location.reload()
        },
        changeOrigin(orgid) {
            this.$store.dispatch(Types.CHANGE_CURRENT_ORG, orgid)
        },
        showModifyPwd() {
            this.$emit('showModifyPwd')
        },
        toVisit() {
            this.$router.push('/nb-return-visit')
        },
        async passHandler() {
            this.openId = this.user.openid
            if (!this.openId) {
                this.$message.error('未绑定微信')
                return
            } else {
                let model = {
                    wechatCode: 'manage',
                    openId: this.openId
                }
                const {openIdToken} = await this.$openApiService.openId2OpenIdToken(model)
                LS.put('openIdToken', openIdToken)
                window.open(this.url, '_blank')
            }
        },
        async reportHandle() {
            window.open(this.reportUrl, '_blank')
        }
    },
    computed: {
        url() {
            return this.urlMapping[DOMAIN.ENV]
        },
        reportUrl() {
            return this.reportUrlMapping[DOMAIN.ENV]
        },
        ...mapGetters(['user', 'token', 'currentOrgId', 'allOrg', 'allOrgMap', 'unReadMessage', 'visitCount', 'todoNumber'])
    }
}
</script>
<style lang="less" rel="stylesheet/less">
@header-height： 60px;
.current-org{
    font-size: 1.1em;
    color: #fff;
    display: flex;
    align-items: center;
    height: 100%;
}
.boss-header{
    height: @header-height;
    background: @sencond-color;
    color: #fff;
    display: flex;
    justify-content: space-between;
    &>div{
        display: flex;
        height: @header-height;
    }
    .header-item{
        height: @header-height;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0 @global-padding;
        position: relative;
        &:hover{
            background: darken(@sencond-color, 10%);
            cursor: pointer;
            .qr-code-fixed{
                display: block;
            }
        }
        &.readonly:hover{
            background: inherit;
            cursor: default;
        }
    }
}
</style>
