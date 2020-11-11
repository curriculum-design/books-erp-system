<template lang="pug">
.login-bg(v-loading="fullLoading" element-loading-text="正在登陆中...")
    .login-title 图书馆进销存管理系统
    el-tabs(type="border-card")
        <!--el-tab-pane(label="钉钉扫码")-->
            <!--#ding-talk-login(v-loading="containerLoading")-->
        el-tab-pane(label="账号登陆")
            .login-view
                el-form(@submit.native.prevent="submitHandler()")
                    .login-input-group
                        .input-label 用户名
                        input.margin-top-10(v-model="username" placeholder="请输入用户名")
                        .input-label 密码
                        input(type="password" v-model="password" placeholder="请输入密码")
                    el-button.login-button(native-type="submit", :plain="true", :loading="loading" :fullscreen="true", :append-to-body="true", top="0") 登录
    VersionFooter
</template>

<script>
import Types from '../../store/types'
import VersionFooter from '@/components/version-footer'
// import scriptjs from 'scriptjs'
import QueryString from 'query-string'
// import HtmlUtils from '@/utils/HtmlUtils'
// const APP_ID_MAP = {
//     'local': 'dingoa29isdnhf0mi2rbyp',
//     'dev': 'dingoavra1jtcecz4qiqxh',
//     'test': 'dingoa29jfsv3hpu8seaba',
//     'pro': 'dingoaweu98fy2swix09yf',
// }
export default {
    components: {VersionFooter},
    data() {
        return {
            loading: false,
            containerLoading: false,
            username: '',
            password: '',
            fullLoading: false,
            dingdingLoading: true,
        }
    },
    computed: {
        redirect() {
            return this.$route.query.redirect
        },
        token() {
            return this.$route.params.token
        },
        code() {
            let {code} = QueryString.parse(location.search)
            const codeKey = 'code-item-key' + code
            if (localStorage.getItem(codeKey)) {
                return null
            }
            localStorage.setItem(codeKey, code)
            return code
        },
    },
    async mounted() {
        if (this.token) {
            this.fullLoading = true
            try {
                await this.submitHandler(this.token)
            } finally {
                this.fullLoading = false
            }
        } else if (this.code) {
            try {
                this.fullLoading = true
                const token = await this.getTokenByCode(this.code)
                this.submitHandler(token)
            } catch ({msg}) {
                this.$alert(msg)
                this.initLoginContainer()
            } finally {
                this.fullLoading = false
            }
        } else {
            this.initLoginContainer()
        }
    },
    methods: {
        // async getTokenByCode(code) {
        //     const token = await this.$loginService.loginByDingTalk({code: code})
        //     return token
        // },
        initLoginContainer() {
            // const redirectUri = window.location.origin + '/new-boss/system/'

            // if (HtmlUtils.isMobile()) {
            //     const gotoUrl = `https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=${APP_ID_MAP[DOMAIN.ENV]}&response_type=code&scope=snsapi_auth&state=STATE&redirect_uri=${redirectUri}`
            //     location.href = gotoUrl
            // } else {
            //     const gotoUrl = `https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=${APP_ID_MAP[DOMAIN.ENV]}&response_type=code&scope=snsapi_login&state=STATE&redirect_uri=${redirectUri}`
            //     const encodeGoto = encodeURIComponent(gotoUrl)
            //     this.containerLoading = true
            //     scriptjs(['//g.alicdn.com/dingding/dinglogin/0.0.5/ddLogin.js'], () => {
            //         this.$nextTick(() => {
            //             window.DDLogin({
            //                 id: 'ding-talk-login',
            //                 goto: encodeGoto,
            //                 style: 'border:none;background-color:#FFFFFF;',
            //                 width: '320px',
            //                 height: '360px'
            //             })
            //             this.containerLoading = false
            //             const hanndleMessage = (event) => {
            //                 const origin = event.origin
            //                 if (origin === 'https://login.dingtalk.com') { // 判断是否来自ddLogin扫码事件。
            //                     const loginTmpCode = event.data // 拿到loginTmpCode后就可以在这里构造跳转链接进行跳转了
            //                     location.href = `https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=APPID&response_type=code&scope=snsapi_login&state=bosslogin&redirect_uri=${encodeGoto}&loginTmpCode=${loginTmpCode}`
            //                 }
            //             }
            //             window.addEventListener('message', hanndleMessage, false)
            //         })
            //     })
            // }
        },
        async submitHandler(paramsToken) {
            try {
                this.loading = true
                let data = paramsToken || await this.$loginService.login(this.username, this.password)
                await this.$store.dispatch(Types.LOGIN, data.token)
                if (this.redirect) {
                    location.href = this.redirect
                } else {
                    this.$router.push('/')
                }
            } catch (e) {
                if (e.code === 401) {
                    this.$message('用户名或者密码错误!')
                } else {
                    console.log(e)
                    this.$message(`${e.msg}`)
                }
            } finally {
                this.$nextTick(() => {
                    this.loading = false
                })
            }
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0 1000px white inset;  //使用足够大的纯色内阴影覆盖黄色背景
    -webkit-text-fill-color: #000 !important;
    transition: background-color 5000s ease-in-out 0s;
}
.login-bg{
    @baseback: #0bc6ff;
    background: linear-gradient(145deg, darken(@baseback, 50%), darken(@baseback, 35%), darken(@baseback, 15%));
    text-align: center;
    height: 100vh;
    width: 100vw;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    font-weight: lighter;
    .version-footer{
        color: #fff;
    }
    .login-title{
        font-size: 2.5em;
        margin-bottom: 30px;
        color: #fff;
    }
    .el-tab-pane{
        height: 360px;
        width: 320px;
        overflow: hidden;
    }
    .login-view{
        width: 320px;
        height: 100%;
        text-align: left;
        .title{
            font-size: 2em;
        }
        .login-input-group{
            margin: @global-padding*2 0;
        }
        input{
            display: block;
            width: 100%;
            color: inherit;
            font-size: 1.2em;
            margin-bottom: @global-padding;
            border: 0;
            border-bottom: 1px solid #bdbdbd;
            padding: @global-padding;
            padding-left: 0;
            outline:none;
        }
        button{
            width: 100%;
            font-size: 1.2em;
            padding: @global-padding 0;
            // background: none;
            border-radius: 0;
            // color: inherit;
            // &.is-plain:hover, &.is-plain:focus{
            //     background: none;
            //     color: inherit;
            //     border-color: inherit;
            // }
        }
    }
}
</style>
