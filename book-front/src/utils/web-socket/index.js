// import WebSocketClient from '@front-common/web-socket-js'
// import DOMAIN from '../DOMAIN'
// let path = '/web-socket'
// if (DOMAIN.IS_PRO) {
//     path = 'https://ws.shanhs.com/web-socket'
// } else if (DOMAIN.IS_TEST) {
//     path = 'https://test.www.shanhs.com.cn/web-socket'
// } else {
//     path = 'https://dev.www.shanhs.com.cn/web-socket'
// }
// const client = new WebSocketClient(path)
// window.socketClient = client
// export default {
//     initClient(token) {
//         client.setToken(token)
//         client.initClient()
//         client.client.debug = false
//     },
//     client,
//     topic: {
//         NB_RETURN_VISIT: '/topic/notice/nb-return-visit',
//         NB_MESSAGE_NOTIFY: '/topic/notice/nb-message-notify',
//         RISK_ORDER_CLAIM_STATUS: '/topic/notice/risk-order-claim-notice',
//         APP_VERSION_BOSS: '/topic/notice/app-version/BOSS'
//     },
//     registerUserHandle(type, handle) {
//         client.registerUserHandle(type, handle)
//     },
//     registerHandle(type, handle) {
//         client.registerHandle(type, handle)
//     }
// }
