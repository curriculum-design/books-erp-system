import PhoneSelectDialog from './index.vue'
import Vue from 'vue'
import store from '../../store'

const PhoneSelectDialogConstructor = Vue.extend(PhoneSelectDialog)

const $vm = new PhoneSelectDialogConstructor({
    el: document.createElement('div'),
    store,
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

Vue.prototype.$phoneSelect = PhoneSelect
export default PhoneSelect
