import TakePhotoDialog from './index.vue'
import Vue from 'vue'

const TakePhotoDialogConstructor = Vue.extend(TakePhotoDialog)

const $vm = new TakePhotoDialogConstructor({
    el: document.createElement('div'),
})

document.body.appendChild($vm.$el)

const takePhoto = ({single = false} = {}) => {
    $vm.show = true
    $vm.single = single
    return new Promise((resolve, reject) => {
        $vm.$off('success')
        $vm.$on('success', val => {
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

Vue.prototype.$takePhoto = takePhoto
export default takePhoto
