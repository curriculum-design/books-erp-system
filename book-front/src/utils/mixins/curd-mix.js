export default (serviceName) => {
    return {
        data() {
            return {
                loading: false,
                editLoading: false,
            }
        },
        computed: {
            api() {
                return this[serviceName]
            },
        },
        methods: {
            save(data) {
                return this.api.save(data)
            },
            update(data) {
                return this.api.update(data)
            },
            del(data) {
                return this.api.del(data)
            },
            list(data) {
                return this.api.list(data)
            },
            get(data) {
                return this.api.get(data)
            },
            async delConfirm(data) {
                await this.$confirm('你确定要删除吗?', '提示')
                try {
                    await this.del(data)
                    this.$message.success('删除成功')
                } catch (e) {
                    throw e
                }
                return data
            },
            async revConfirm(data) {
                await this.$confirm('你确定要恢复吗?', '提示')
                try {
                    await this.del(data)
                    this.$message.success('恢复成功')
                } catch (e) {
                    throw e
                }
                return data
            },
            errorCatch(e) {
                throw e
            },
            async submit(data, primary) {
                this.editLoading = true
                try {
                    let result
                    if (data[primary]) {
                        result = await this.update(data)
                    } else {
                        result = await this.save(data)
                    }
                    return result
                } catch (e) {
                    this.errorCatch(e)
                } finally {
                    this.$nextTick(() => {
                        this.editLoading = false
                    })
                }
            }
        }
    }
}
