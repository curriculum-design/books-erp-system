import ImagePreviewDialog from './index.vue'
import Vue from 'vue'

const ImagePreviewDialogConstructor = Vue.extend(ImagePreviewDialog)

const $vm = new ImagePreviewDialogConstructor({
    el: document.createElement('div'),
})

document.body.appendChild($vm.$el)

const ImagePreview = (src, size) => {
    $vm.show = true
    $vm.size = size
    $vm.src = src
    $vm.$on('update:show', show => {
        $vm.show = show
    })
    return $vm
}

Vue.prototype.$imagePreview = ImagePreview
export default ImagePreview
