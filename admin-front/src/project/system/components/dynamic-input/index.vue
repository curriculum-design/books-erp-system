<template lang="pug">
  div.dynamic-input
      components(:is="componentName", :placeholder="placeholder", v-model="privateValue", :type="type === 6 ? 'datetime' : undefined" @change="$emit('change')")
          template(v-if="componentName === 'el-checkbox-group'")
              el-checkbox(v-for="item in items", :label="item.value", :key="item.value") {{item.name + (showItemValue ? ` - ${item.value}` : '')}}
          template(v-if="componentName === 'el-radio-group'")
              el-radio(v-for="item in items", :label="item.value", :key="item.value") {{item.name + (showItemValue ? ` - ${item.value}` : '')}}
      p.margin-top-20(v-if="showRealValue")
        el-input(v-model="realValue")
            template(slot="prepend") 真实值
      p.margin-top-20(v-if="showValue")
          el-input(v-model="valueNames" readonly)
              template(slot="prepend") 配置参数
</template>
<script>
import dateformat from 'dateformat'
import CommonUtils from '@/utils/CommonUtils'
const typeComponentMap = {
    1: 'el-input',
    2: 'el-input-number',
    3: 'el-checkbox-group',
    4: 'el-radio-group',
    5: 'el-date-picker',
    6: 'el-date-picker',
    7: 'SelectImage'
}
export default {
    props: {
        showItemValue: {
            default: false,
        },
        placeholder: {
            default: '',
        },
        items: {
            defualt: () => [],
        },
        showRealValue: {
            default: false,
        },
        showValue: {
            default: false,
        },
        type: {
            default: 1,
        },
        value: {
            default: () => [],
        },
    },
    computed: {
        itemMap() {
            return CommonUtils.arrayToMap(this.items, 'value', 'name')
        },
        valueNames() {
            const realValue = this.realValue
            if (realValue.split) {
                const values = realValue.split(',')
                return values.map(v => {
                    const name = this.itemMap[v]
                    if (name) {
                        return `${name}` + (this.showItemValue ? ` - ${v}` : '')
                    }
                    return v
                }).join(',')
            }
            return realValue
        },
        realValue: {
            get() {
                if (Array.isArray(this.privateValue)) {
                    return this.privateValue ? this.privateValue.join(',') : ''
                }
                return this.privateValue
            },
            set(val) {
                this.privateValue = val
            }
        },
        isSelect() {
            return (this.type === 3) && typeof this.value === 'string'
        },
        privateValue: {
            get() {
                if (this.isSelect) {
                    return this.value ? this.value.split(',') : []
                }
                return this.value
            },
            set(val) {
                if (val instanceof Date) {
                    let format = 'yyyy-mm-dd'
                    if (this.type === 6) {
                        format = 'yyyy-mm-dd HH:mm:ss'
                    }
                    val = dateformat(val, format)
                }
                if (Array.isArray(val)) {
                    val = val.filter(v => this.itemMap[v] !== undefined)
                }
                this.$emit('input', val)
            }
        },
        componentName() {
            return typeComponentMap[this.type]
        }
    },
    methods: {
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.dynamic-input{
    .el-checkbox+.el-checkbox{
        margin-left: 0;
    }
    .el-radio+.el-radio{
        margin-left: 0;
    }
    .el-checkbox{
        margin-right: 20px;
    }
    .el-radio{
        margin-right: 20px;
    }
}
</style>
