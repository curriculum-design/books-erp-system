import OptionSelectDialog from './index.vue'
import Vue from 'vue'

const OptionSelectDialogConstructor = Vue.extend(OptionSelectDialog)

const $vm = new OptionSelectDialogConstructor({
    el: document.createElement('div'),
})

document.body.appendChild($vm.$el)

const OptionSelect = ({single = false} = {}) => {
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

Vue.prototype.$optionSelect = OptionSelect
export default OptionSelect
