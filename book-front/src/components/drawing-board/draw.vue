<template lang="pug">
  .drawing-board-draw
      .draw
          canvas.main-draw(ref="mainCanvas", :height="height", :width="width")
          canvas.second-draw(ref="secondCanvas", :height="height", :width="width"
          @mousedown="mousedown" @mousemove="mousemove" @mouseup="mouseup")
      TextEdit(@close="textShow = false"
      @yes="inputText"
      :color="color",
      :fontSize="fontSize"
      v-if="textShow", :style.native="`left: ${startX - 10}px; top: ${startY - 10}px`")
</template>

<script>
import TextEdit from './text-edit'
const DRAW_TYPES = {
    PENCEL: 1,
    LINE: 2,
    SQUARE: 3,
    CIRCLE: 4,
    TEXT: 5,
    ERASER: 6,
}
export default {
    components: {TextEdit},
    DRAW_TYPES,
    props: {
        value: {default: ''},
        color: {defualt: '#000'},
        lineWidth: {default: 2},
        drawType: {default: DRAW_TYPES.PENCEL},
        background: {},
        height: {defualt: 500},
        width: {default: 600},
    },
    data() {
        return {
            fontSize: 16,
            textShow: false,
            canvasTop: 0,
            canvasLeft: 0,
            startDraw: false,
            startX: 0,
            startY: 0,
            stepStore: [],
            currentIndex: 0,
        }
    },
    computed: {
        mainCanvas() {
            return this.$refs.mainCanvas
        },
        mainContext() {
            return this.mainCanvas.getContext('2d')
        },
        secondCanvas() {
            return this.$refs.secondCanvas
        },
        secondContext() {
            return this.secondCanvas.getContext('2d')
        },
    },
    watch: {
        currentIndex(val) {
            this.$emit('change', this.stepStore[val - 1])
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.clearMainCanvas()
        })
    },
    methods: {
        drawTextToCanvas(val) {
            let ctx = this.secondContext
            ctx.font = `bold ${this.fontSize}px arial`
            ctx.textAlign = 'left'
            ctx.textBaseline = 'top'
            ctx.fillStyle = this.color
            ctx.fillText(val, this.startX, this.startY)
            this.storeSecondDraw()
        },
        inputText(val) {
            this.textShow = false
            this.drawTextToCanvas(val)
        },
        mousedown(e) {
            const {x, y} = this.getPosition(e)
            this.startX = x
            this.startY = y
            if (this.drawType === DRAW_TYPES.TEXT) {
                this.textShow = true
            } else {
                const {secondContext} = this
                secondContext.lineJoin = 'round'
                secondContext.lineWidth = this.lineWidth
                secondContext.strokeStyle = this.color
                secondContext.beginPath()
                secondContext.moveTo(x, y)
                this.startDraw = true
                e.preventDefault()
            }
        },
        mousemove(e) {
            if (this.startDraw) {
                this.drawWithType(e)
            }
        },
        clear() {
            this.clearSecondCanvas()
            this.clearMainCanvas()
            this.stepStore = []
            this.currentIndex = 0
        },
        clearMainCanvas() {
            const {mainContext, mainCanvas} = this
            mainContext.clearRect(0, 0, mainCanvas.width, mainCanvas.height)
            // 画入背景
            if (this.background) {
                this.drawImageToCanvas(this.background, mainCanvas)
            }
        },
        clearSecondCanvas() {
            const {secondContext, secondCanvas} = this
            secondContext.clearRect(0, 0, secondCanvas.width, secondCanvas.height)
        },
        drawWithType(e) {
            const {secondContext, mainContext, startX, startY} = this
            const {x, y} = this.getPosition(e)
            switch (this.drawType) {
            case DRAW_TYPES.PENCEL:
                secondContext.lineTo(x, y)
                secondContext.stroke()
                break
            case DRAW_TYPES.LINE:
                this.clearSecondCanvas()
                secondContext.beginPath()
                secondContext.moveTo(startX, startY)
                secondContext.lineTo(x, y)
                secondContext.stroke()
                break
            case DRAW_TYPES.CIRCLE:
                const roundness = Math.sqrt((startX - x) * (startX - x) + (startY - y) * (startY - y))
                this.clearSecondCanvas()
                secondContext.beginPath()
                secondContext.arc(startX, startY, roundness, 0, Math.PI * 2)
                secondContext.stroke()
                break
            case DRAW_TYPES.SQUARE:
                this.clearSecondCanvas()
                secondContext.beginPath()
                secondContext.rect(startX, startY, x - startX, y - startY)
                secondContext.stroke()
                break
            case DRAW_TYPES.ERASER:
                mainContext.clearRect(x - this.lineWidth, y - this.lineWidth, this.lineWidth * 2, this.lineWidth * 2)
                break
            default:
                break
            }
        },
        mouseup(e) {
            this.storeSecondDraw()
        },
        storeSecondDraw() {
            this.startDraw = false
            this.mainContext.drawImage(this.secondCanvas, 0, 0)
            this.clearSecondCanvas()
            this.storeDraw()
        },
        storeDraw() {
            this.mainContext.drawImage(this.secondCanvas, 0, 0)
            const dataUrl = this.mainCanvas.toDataURL('image/jpeg', 1)
            this.stepStore.splice(this.currentIndex, this.stepStore.length)
            this.stepStore.push(dataUrl)
            if (this.stepStore.length > 20) {
                this.stepStore.shift()
            }
            this.currentIndex = this.stepStore.length
        },
        backStep() {
            this.resetIndex(this.currentIndex - 1)
        },
        forwardStep() {
            this.resetIndex(this.currentIndex + 1)
        },
        drawImageToCanvas(src, canvas) {
            const img = new Image()
            img.onload = () => {
                canvas.getContext('2d').drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height)
            }
            img.src = src
        },
        resetIndex(index) {
            if (this.stepStore.length > 0 && index > 0) {
                const currentStep = this.stepStore[index - 1]
                if (currentStep) {
                    this.currentIndex = index
                    this.drawImageToCanvas(currentStep, this.mainCanvas)
                } else {
                    this.currentIndex = this.stepStore.length - 1
                }
            } else {
                this.clearMainCanvas()
                this.currentIndex = 0
            }
        },
        getPosition({clientX, clientY}) {
            const rect = this.mainCanvas.getBoundingClientRect()
            const canvasTop = rect.top + window.pageXOffset
            const canvasLeft = rect.left + window.pageXOffset
            const x = clientX - canvasLeft
            const y = clientY - canvasTop
            return {x, y}
        }
    }
}
</script>
<style lang="less" rel="stylesheet/less">
.drawing-board-draw{
    position: relative;
    .text-edit-container{
        position: absolute;
    }
    .main-draw{
        background: #fff;
    }
    .second-draw{
        position: absolute;
        top: 0;
        left: 0;
        z-index: 0;
        cursor: url(data:image/gif;base64,R0lGODlhBQAFAJECAP///wAAAP///wAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE5MzA0REQ4MDc4QzNDNzNFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjhGNTRBODVDOUY1OTExREY5RTI3QzMxNkUwMkZFOEE4IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjhGNTRBODVCOUY1OTExREY5RTI3QzMxNkUwMkZFOEE4IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDE4MDExNzQwNzIwNjgxMTkzMDRERDgwNzhDM0M3M0UiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDE4MDExNzQwNzIwNjgxMTkzMDRERDgwNzhDM0M3M0UiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAACACwAAAAABQAFAAACCVQQdnHguNISBQA7) 2 2,crosshair;
    }
}
</style>
