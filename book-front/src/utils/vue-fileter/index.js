/**
 * Created by xiejun on 2017/3/10.
 */
import dateFormat from 'dateformat'
import Vue from 'vue'
import Mapping from '../mapping-plugin/index'
/**
 * 数字转化为千位分隔
 * value | thousand-spilt
 */
Vue.filter('thousand-spilt', function(value) {
    return value.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
})

Vue.filter('idcard', function(num) {
    return num.substring(0, 5) + '*******' + num.substring(num.length - 5)
})

Vue.filter('date-format', function(time, format = 'yyyy-mm-dd HH:MM:ss') {
    if (!time) {
        return ''
    }
    let date = new Date(time)
    return dateFormat(date, format)
})
/**
 * 电话号码隐藏
 */
Vue.filter('phone', function(phone) {
    return phone && (phone.slice(0, 3) + '****' + phone.slice(7, 11))
})

/**
 * 最多显示10个字
 */
Vue.filter('sub-more', function(val, num = 10) {
    if (val.length > num) {
        return val && (val.slice(0, num) + '...')
    } else {
        return val
    }
})

/**
 * 最多显示10个字
 */
Vue.filter('no-data', function(val) {
    if (val === '') {
        return '-'
    } else {
        return val
    }
})

/**
 * 将日期格式化为之前的时间，例如 (刚刚，5分钟前，1小时前，7天前，两周前)
 * @type {[type]}
 */
Vue.filter('beforetime', time => {
    time = new Date(time)
    let now = new Date()
    let timeDiff = now.getTime() - time.getTime()
    let diffSencond = timeDiff / 1000
    if (diffSencond < 60) {
        return '刚刚'
    }
    let diffMinutes = parseInt(diffSencond / 60)
    if (diffMinutes < 60) {
        return diffMinutes + ' 分钟前'
    }
    let diffHours = parseInt(diffMinutes / 60)
    if (diffHours < 24) {
        return diffHours + ' 小时前'
    }
    let diffDay = parseInt(diffHours / 24)
    if (diffDay < 7) {
        return diffDay + ' 天前'
    }
    if (diffDay < 31) {
        return parseInt(diffDay / 7) + ' 周前'
    }
    return dateFormat(time, 'MM月DD日')
})

Vue.filter('parse-int', str => {
    return str && parseInt(str)
})

Vue.filter('mapping', (value, mappingKey) => {
    let mapping = Mapping.Mapping[mappingKey]
    let mappingRev = {}
    Object.keys(mapping).forEach(k => {
        let revValue = mapping[k]
        mappingRev[revValue] = k
    })
    return mappingRev[value]
})
