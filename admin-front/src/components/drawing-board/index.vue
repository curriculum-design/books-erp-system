<template lang="pug">
.drawing-board
    el-popover(ref="linepopover" v-model="linePopoverShow")
        .line-wdith-options
            .item(v-for="width in widthOptions" @click="selectLineSize(width)")
                .item-circle(:style="{height: width + 'px', width: width + 'px'}")
    .toolbar(:style="{width: width + 'px'}")
        i.fa.fa-pencil(@click="changeOperator(drawTypes.PENCEL)")
        i.fa.fa-circle(v-popover:linepopover="")
        i
            el-color-picker(v-model="currentColor")
        i.fa.fa-eraser(@click="changeOperator(drawTypes.ERASER)")
        span.split |
        i(@click="changeOperator(drawTypes.TEXT)")
            b T
        i(@click="changeOperator(drawTypes.LINE)")
            b /
        i.fa.fa-circle-o(@click="changeOperator(drawTypes.CIRCLE)")
        i.fa.fa-square-o(@click="changeOperator(drawTypes.SQUARE)")
        span.split |
        i.fa.fa-reply(@click="$refs.draw.backStep()")
        i.fa.fa-share(@click="$refs.draw.forwardStep()")
        i.fa.fa-trash-o(@click="$refs.draw.clear()")
    .draw-container
        Draw(:height="height", :width="width", :drawType="currentOpreator", :color="currentColor", ref="draw",
        :background = "background"
        :lineWidth="currentLineSize" @change="drawChange")
</template>

<script>
import Draw from './draw'
export default {
    components: {Draw},
    props: {
        height: {
            default: 600,
        },
        width: {
            default: 600,
        },
        background: {
            default: '',
        }
    },
    data() {
        return {
            currentOpreator: Draw.DRAW_TYPES.PENCEL,
            currentLineSize: 2,
            currentColor: '#FF0000',
            widthOptions: [2, 4, 8, 12, 16, 18],
            linePopoverShow: false,
            drawValue: '',
        }
    },
    computed: {
        drawTypes() {
            return Draw.DRAW_TYPES
        }
    },
    methods: {
        selectLineSize(width) {
            this.currentLineSize = width
            this.linePopoverShow = false
        },
        init() {
        },
        drawChange(dataUrl) {
            this.$emit('change', dataUrl)
        },
        changeOperator(operator) {
            this.currentOpreator = operator
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
@hover-color:  rgba(131, 131, 131, 0.2);
.line-wdith-options{
    .item{
        display: block;
        padding: 5px;
        cursor: pointer;
        text-align: center;
        &:hover{
            background: @hover-color;
        }
        &>.item-circle{
            display: inline-block;
            border-radius: 50%;
            background: #000;
        }
    }
}
.drawing-board{
    height: 100%;
    width: 100%;
    .toolbar{
        display: flex;
        justify-content: space-around;
        align-items: center;
        i{
            font-size: 1.2em;
            cursor: pointer;
            width: 2em;
            height: 2em;
            text-align: center;
            display: flex;
            align-items: center;
            justify-content: center;
            &:hover{
                background: @hover-color;
            }
        }
        .split{
            color: @hover-color;
        }
    }
}
</style>
