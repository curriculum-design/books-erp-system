export default {
    props: ['show', 'value'],
    computed: {
        form: {
            get() {
                return this.value
            },
            set(val) {
                this.$emit('input', val)
            }
        },
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
        submitHandler() {
            this.$emit('submit', this.form)
        }
    }
}
