export default {
    props: ['show'],
    computed: {
        dialogShow: {
            get() {
                return this.show
            },
            set(val) {
                this.$emit('update:show', val)
            }
        }
    },
    methods: {
        close() {
            this.dialogShow = false
        }
    }
}
