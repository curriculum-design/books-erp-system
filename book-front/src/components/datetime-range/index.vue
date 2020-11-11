<template lang="pug">
  el-date-picker(v-model="dateValue" type="datetimerange",
  :picker-options="pickerOptions" placeholder="选择时间范围")
</template>

<script>
export default {
    props: ['value'],
    computed: {
        dateValue: {
            get() {
                let [begin, end] = this.value
                return [begin && new Date(begin), end && new Date(end)]
            },
            set([begin, end]) {
                return this.$emit('input', [begin, end])
            }
        },
        pickerOptions() {
            return {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date()
                        const start = new Date()
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
                        picker.$emit('pick', [start, end])
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date()
                        const start = new Date()
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
                        picker.$emit('pick', [start, end])
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date()
                        const start = new Date()
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
                        picker.$emit('pick', [start, end])
                    }
                }]
            }
        },
    }
}
</script>
<style lang="less" rel="stylesheet/less">
</style>
