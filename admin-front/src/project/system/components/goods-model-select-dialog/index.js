import PhoneSelectDialog from './index.vue'
import Vue from 'vue'

const PhoneSelectDialogConstructor = Vue.extend(PhoneSelectDialog)

const $vm = new PhoneSelectDialogConstructor({
    el: document.createElement('div'),
})

document.body.appendChild($vm.$el)

const PhoneSelect = ({single = false} = {}) => {
    $vm.show = true
    $vm.single = single
    return new Promise((resolve, reject) => {
        $vm.$off('select')
        $vm.$on('select', val => {
            resolve(val)
        })
        $vm.$off('update:show')
        $vm.$on('update:show', show => {
            $vm.show = show
            if (!show) {
                reject()
            }
        })
    })
}

Vue.prototype.$goodsModelSelect = PhoneSelect
export default PhoneSelect
