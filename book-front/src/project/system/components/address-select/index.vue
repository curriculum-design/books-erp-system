<template lang="pug">
    el-cascader(v-model="selectAddr", :options="options", expand-trigger="hover" @active-item-change="handleItemChange")
</template>

<script>
    export default {
        props: ['value', 'notArea'],
        data() {
            return {
                options: [],
                cache: {},
            }
        },
        watch: {
            value(val) {
                this.handleItemChange(val)
            }
        },
        created() {
            this.getArea().then(province => {
                this.options = province
                this.handleItemChange(this.value)
            })
        },
        methods: {
            async handleItemChange([province, city, area]) {
                let proKey = `p-${province}`
                let cityKey = `c-${city}`
                this.cache[proKey] = this.cache[proKey] || await this.getArea(province)
                let citys = this.cache[proKey]
                this.options.forEach(item => {
                    if (item.label === province) {
                        item.children = citys
                    }
                })

                if (city) {
                    this.cache[cityKey] = this.cache[cityKey] || await this.getArea(city, false)
                    let areas = this.cache[cityKey]
                    citys.forEach(item => {
                        if (item.label === city) {
                            item.children = areas
                        }
                    })
                }
            },
            async getArea(parent, children = true) {
                try {
                    if (this.notArea && !children) {
                        return [
                            {
                                label: '全部',
                                value: '全部',
                            }
                        ]
                    }
                    let areas = await this.$api.bsSfarea.list({parent})
                    return areas.map(a => {
                        let addr = {
                            label: a,
                            value: a,
                        }
                        if (children) addr.children = []
                        return addr
                    })
                } catch (e) {
                    console.error(e)
                } finally {
                }
            }
        },
        computed: {
            selectAddr: {
                get() {
                    return this.value
                },
                set(val) {
                    this.$emit('input', val)
                }
            }
        }
    }
</script>
<style lang="less" rel="stylesheet/less">
</style>
