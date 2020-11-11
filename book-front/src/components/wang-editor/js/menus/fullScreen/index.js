/*
    full-screen-menu
*/
import $ from '../../util/dom-core.js'

// 构造函数
function FullScreen(editor) {
    this.editor = editor
    this.$elem = $(
        `<div class="w-e-menu">
            <i class="fa fa-arrows-alt"><i/>
        </div>`
    )
    this.type = 'click'

    // 当前是否 active 状态
    this._active = false
}

// 原型
FullScreen.prototype = {
    constructor: FullScreen,

    // 点击事件
    onClick: function (e) {
        // 点击菜单将触发这里

        const editor = this.editor

        // 执行 full-screen 命令
        editor.config.fullScreen()
    }
}

export default FullScreen
