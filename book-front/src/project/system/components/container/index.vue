<template lang="pug">
  .main-container
      BHeader.header(@menuToggle="menuShow = !menuShow", @showModifyPwd="showModifyPwd" @setMenuHidden="setMenuHidden")
      .container
          HiddenComponent.shanhs-menu-nav(v-model="menuShow")
              NavMenu.nav-menu(:navMenu="menuData" @menuClick="menuClickHandler")
          .right.main-content(:md="menuShow ? 20 : 24")
              router-view(:key="$route.path")
      el-dialog(title="修改密码", :visible.sync="modifyPwdShow", v-if="modifyPwdShow", :modal-append-to-body="true", :close-on-click-modal="false", v-loading="modifyPwdLoading")
          el-form(label-width="150px")
              el-form-item(label="新密码")
                  el-input(type="password" v-model="pwdform.password")
              el-form-item(label="再次输入新密码")
                  el-input(type="password" v-model="pwdform.password2")
              el-form-item(label="旧密码")
                  el-input(type="password" v-model="pwdform.oldPassword")
              div(style="width: 100%; text-align: right")
                  el-button(type="primary", @click="modifyPwd") 确定修改
</template>

<script>
import BHeader from './container-header'
import NavMenu from '@/components/nav-menu'
import MenuDataMix from '../../mixins/menu-data-mix'
import HtmlUtils from '@/utils/HtmlUtils'
export default {
    components: {BHeader, NavMenu},
    mixins: [MenuDataMix],
    data() {
        return {
            menuShow: true,
            modifyPwdShow: false,
            modifyPwdLoading: false,
            pwdform: {
                password: '',
                oldPassword: '',
                password2: ''
            },
        }
    },
    methods: {
        setMenuHidden(val) {
            this.menuShow = false
        },
        menuClickHandler() {
            if (HtmlUtils.isMobile()) {
                this.menuShow = false
            }
        },
        showModifyPwd() {
            this.modifyPwdShow = true
        },
        async modifyPwd() {
            if (this.pwdform.password.length < 6 || this.pwdform.password2.length < 6) {
                this.$message.error('请将密码填写完整')
                return
            }
            if (this.pwdform.oldPassword.length < 6) {
                this.$message.error('请将旧密码填写完整')
                return
            }
            if (this.pwdform.password !== this.pwdform.password2) {
                this.$message.error('第一次输入密码和第二次输入密码不一致')
                return
            }
            try {
                this.modifyPwdLoading = true
                await this.$api.bsUser.updatePassword(this.pwdform)
                this.$message.success('修改成功')
                this.modifyPwdShow = false
            } finally {
                this.modifyPwdLoading = false
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.main-container{
    height: 100vh;
    padding-top: @header-height;
    .header{
        position: absolute;
        width: 100%;
        top: 0;
        left: 0;
    }
    .nav-menu{
        width: 200px;
    }
    .container{
        display: flex;
        flex-flow: row;
        height: 100%;
        overflow: auto;
        display: flex;
        &>div{
            height: 100%;
        }
        .main-content{
            padding: @global-padding;
            padding-bottom: 0;
            flex: 1;
            height: 100%;
            overflow: auto;
        }
        .left {
            width: 210px;
        }
        .right {
            flex: 1
        }
    }
}
</style>
