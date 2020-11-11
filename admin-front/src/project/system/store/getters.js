import CommonUtils from '@/utils/CommonUtils'
// import WebSocket from '@/utils/web-socket'
export default {
    // webSocket: WebSocket.client,
    isLogin: state => state.isLogin,
    user: state => state.user,
    token: state => state.token,
    permission: state => state.permission,
    permissionSigns: state => state.permission.map(p => p.sign),
    unReadMessage: state => state.unReadMessage,
    visitCount: state => state.visitCount,
    todoNumber: state => state.todoNumber,
    allProduct: state => state.allProduct,
    allProductMap: state => CommonUtils.arrayToMap(state.allProduct, 'prdid', 'prdname'),
    allCtg: state => {
        const dataMap = {}
        state.allProduct.forEach(({ctgid, ctgname}) => {
            dataMap[ctgid] || (
                dataMap[ctgid] = {
                    ctgid,
                    ctgname
                }
            )
        })
        return Object.values(dataMap)
    },
    allChannel: state => state.allChannel,
    allChannelMap: state => CommonUtils.arrayToMap(state.allChannel, 'channelid', 'channelname'),
    allOrg: state => state.allOrg,
    allOrgMap: state => CommonUtils.arrayToMap(state.allOrg, 'orgid', 'orgname'),
    allSysParam: state => {
        let allParam = state.allSysParam
        let map = CommonUtils.arrayToArrayMap(allParam, 'paramctgcode')
        return map
    },
    allSysParamMap: state => {
        let allParam = state.allSysParam
        let map = CommonUtils.arrayToArrayMap(allParam, 'paramctgcode')
        Object.keys(map).forEach(code => {
            let paramArray = map[code]
            map[code] = CommonUtils.arrayToMap(paramArray, 'paramvalue', 'paramcode')
        })
        return map
    },
    allSysParamMapRev: state => {
        let allParam = state.allSysParam
        let map = CommonUtils.arrayToArrayMap(allParam, 'paramctgcode')
        Object.keys(map).forEach(code => {
            let paramArray = map[code]
            map[code] = CommonUtils.arrayToMap(paramArray, 'paramcode', 'paramvalue')
        })
        return map
    },
    systemMapping: state => {
        let allParam = state.allSysParam
        let map = CommonUtils.arrayToArrayMap(allParam, 'paramType')
        Object.keys(map).forEach(code => {
            let paramArray = map[code]
            map[code] = CommonUtils.arrayToMap(paramArray, 'paramLabel', 'paramValue')
        })
        const proxy = new Proxy(map, {
            get(target, key) {
                key = key.replace(/([A-Z])/g, '_$1').toUpperCase()
                return target[key] || {}
            }
        })
        return proxy
    },
    currentOrgId: state => state.currentOrgId,
    allDetails: state => state.allDetails,
    allDetailNameMap: state => {
        return CommonUtils.arrayToMap(state.allDetails, 'id', 'detailName')
    },
    allDetailMap: state => {
        return CommonUtils.arrayToObjectMap(state.allDetails, 'id')
    },
    allParentDetailMap: state => {
        return CommonUtils.arrayToArrayMap(state.allDetails, 'parentId')
    },
    currentMenu: state => {
        return state.currentMenu
    },
    allPermissionUrlMap: state => {
        return CommonUtils.arrayToObjectMap(state.allPermission, 'url')
    }
}
