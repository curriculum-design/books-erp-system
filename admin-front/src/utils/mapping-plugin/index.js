import Const from '@/utils/Const'

const Mapping = {
    menuType: {
        [Const.MENU_TYPE_MENU]: '菜单',
        [Const.MENU_TYPE_BUTTUN]: '按钮',
        [Const.MENU_TYPE_OTHER]: '其他'
    },
    imageFrom: {
        1: '用户上传',
        2: '网络图片',
    },
    bizOrderStatus: {
        0: '暂存',
        1: '待审核',
        2: '待检测',
        10: '已完成'
    },
    bizOrderPayStatus: {
        0: '未付款',
        1: '已付款',
        2: '部分付款'
    }
}
const freeze = Object.freeze || (e => e)
export default {
    Mapping: freeze(Mapping),
    install(Vue) {
        Vue.mixin({
            computed: {
                $mapping: () => freeze(Mapping),
                $reverseMapping: function() {
                    let m = {}
                    Object.keys(this.$mapping).forEach(mk => {
                        const vs = this.$mapping[mk]
                        let mv = {}
                        Object.keys(vs).forEach(k => {
                            if (isNaN(k)) {
                                mv[k] = vs[k]
                            } else {
                                mv[vs[k]] = parseInt(k)
                            }
                        })
                        m[mk] = mv
                    })
                    return m
                },
                $const: () => freeze(Const),
            }
        })
    }
}
