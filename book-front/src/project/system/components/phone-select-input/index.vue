<template lang="pug">
.phone-select-input-container
    el-input(readonly v-model="privateValueText" clearable @click.native="selectPhoneInputClick" placeholder="请选择机型", :type="multiple ? 'textarea' : 'text'")
    PhoneSelectDialog(:single="single", v-model="this.value", @select="selectPhoneHandle", :show.sync="selectPhoneShow" v-if="selectPhoneShow")
</template>

<script>
import PhoneSelectDialog from '../phone-select-dialog/index.vue'
export default {
    props: {
        value: {
            // type: Array,
            default: () => {
                return []
            }
        },
        single: {default: true},
        canClick: {default: true},
        multiple: {default: false}
    },
    // props: ['value', 'single'],
    components: {PhoneSelectDialog},
    watch: {
        value(val) {
            this.valueChange(val)
        }
    },
    data() {
        return {
            privateValue: '',
            selectPhoneShow: false,
        }
    },
    computed: {
        privateValueText: {
            get() {
                if (this.value instanceof Array && this.value.length) {
                    let prdNames = this.value.map(prdid => {
                        return this.allProductMap[prdid]
                    })
                    return prdNames.join(', ')
                } else if (typeof this.privateValue === 'number') {
                    return this.allProductMap[this.privateValue]
                }
            },
            set(val) {
                this.$emit('input', val)
            }
        },
        allProductMap() {
            return this.$store.getters.allProductMap
        }
    },
    methods: {
        // selectPhoneHandle({prdid}) {
        //     this.$emit('input', prdid)
        // },
        selectPhoneHandle(selectData) {
            if (this.single) {
                this.$emit('input', selectData.prdid)
            } else {
                let prdIds = selectData.map(item => {
                    return item.prdid
                })
                this.$emit('input', prdIds)
            }
        },
        selectPhoneInputClick(e) {
            if (e.target.nodeName === 'I') {
                return
            }
            if (this.canClick) {
                this.selectPhoneShow = true
            }
        },
        valueChange(newVal) {
            this.privateValue = newVal
        }
    }
}
</script>

<style lang="less">
.phone-select-input-container{
    display: inline;
}
</style>
