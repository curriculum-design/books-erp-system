import dateFormat from 'dateformat'
const format = {
    date(time, format = 'yyyy-mm-dd HH:MM:ss') {
        if (!time) {
            return ''
        }
        try {
            let date = new Date(time)
            return dateFormat(date, format)
        } catch (error) {
        }
        return ''
    },
    dateDefault(time) {
        if (!time) {
            return ''
        }
        try {
            let date = new Date(time)
            return dateFormat(date, 'yyyy-mm-dd HH:MM:ss')
        } catch (error) {
        }
        return ''
    },
    dateDay(time) {
        if (!time) {
            return ''
        }
        try {
            let date = new Date(time)
            return dateFormat(date, 'yyyy-mm-dd')
        } catch (error) {
        }
        return ''
    },
    dateMonth(time) {
        if (!time) {
            return ''
        }
        return dateFormat(new Date(time), 'yyyy年mm月')
    }
}
const freeze = Object.freeze || (e => e)
export default {
    install(Vue) {
        Vue.mixin({
            computed: {
                $format: () => freeze(format)
            }
        })
    }
}
