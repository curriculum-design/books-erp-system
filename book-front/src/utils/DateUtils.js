export default {
    getDayStart(date = new Date()) {
        date = new Date(date)
        date.setHours(0)
        date.setMinutes(0)
        date.setSeconds(0)
        return date
    },
    getDayEnd(date = new Date()) {
        date = new Date(date)
        date.setHours(23)
        date.setMinutes(59)
        date.setSeconds(59)
        return date
    },
    addDay(date = new Date(), number) {
        date = new Date(date)
        if (number) {
            date.setDate(date.getDate() + number)
        }
        return date
    },
    getMonthStart(date = new Date()) {
        date = new Date(date)
        return this.getDayStart(new Date(date.getFullYear(), date.getMonth(), 1))
    },
    getMonthEnd(date = new Date()) {
        date = new Date(date)
        return this.getDayEnd(new Date(date.getFullYear(), date.getMonth() + 1, 0))
    },
    getNextMonthStart(date = new Date()) {
        date = new Date(date)
        return this.getDayStart(new Date(date.getFullYear(), date.getMonth() + 1, 1))
    },
    getNextMonthEnd(date = new Date()) {
        date = new Date(date)
        return this.getDayEnd(new Date(date.getFullYear(), date.getMonth() + 2, 0))
    },
    getWeekBegin(date = new Date()) {
        const nowDayOfWeek = date.getDay()
        const nowDay = date.getDate()
        const nowMonth = date.getMonth()
        const nowYear = date.getFullYear()
        return this.getDayStart(new Date(nowYear, nowMonth, nowDay - nowDayOfWeek))
    },
    getWeekEnd(date = new Date()) {
        const nowDayOfWeek = date.getDay()
        const nowDay = date.getDate()
        const nowMonth = date.getMonth()
        const nowYear = date.getFullYear()
        return this.getDayEnd(new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek)))
    }
}
