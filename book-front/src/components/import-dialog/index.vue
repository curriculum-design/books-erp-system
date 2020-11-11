<template lang="pug">
    el-dialog.import-dialog(:title="title", :visible.sync="dialogShow", :fullscreen="true", :append-to-body="true", top="0", @close="$emit('close')")
        p 导入进度 {{successLength + faildLength}} / {{importData.length}}  --- {{parseInt((successLength + faildLength) / importData.length * 100)}}%
        div(v-if="complied")
            p 导入结果 总计 {{importData.length}} 条数据
                el-tag(type="success") 成功 {{successLength}} 条
                el-tag(type="danger") 失败 {{faildLength}} 条
            el-button(type="info" @click="downloadFaildData" v-if="faildLength > 0") 下载失败的数据
        el-button(type="success" @click="beginImport", :loading="loading" v-else) 开始导入
        LocalTable.margin-top-20(:data="processData", border, height="500")
            el-table-column(v-for="header in tableHeader", :prop="header", :label="header", :key="header", show-overflow-tooltip)
            el-table-column(label="导入结果" min-width="300")
                template(slot-scope="scope")
                    .import-result-msg(:class="scope.row._isSuccess ? 'success' : 'faild'" v-if="scope.row._resultMsg")
                        code {{scope.row._resultMsg}}
                    el-tag(v-else type="info") 未导入
</template>

<script>
    import DialogMix from '@/utils/mixins/dialog-mix'
    import ExcelUtils from '@/utils/ExcelUtils'
    export default {
        mixins: [DialogMix],
        props: {
            data: {},
            saveApi: {},
            title: {default: '导入数据'},
            successText: {default: '导入成功'},
            faildText: {default: '导入失败'}
        },
        data() {
            return {
                importResult: [],
                resultData: [],
                loading: false,
            }
        },
        watch: {
            show(val) {
                if (!val) {
                    this.importResult = []
                    this.$nextTick(() => {
                        this.resultData = []
                        this.loading = false
                    })
                }
            },
            importResult(importResult) {
                let rawData = this.rawData
                let resultData = []
                rawData.forEach((item, index) => {
                    let result = importResult[index]
                    item = {...item}
                    if (result) {
                        let resultMsg = ''
                        if (result.success) {
                            resultMsg = this.successText
                        } else {
                            resultMsg = `${this.faildText}: ${result.error.msg}`
                        }
                        item._resultMsg = resultMsg
                        item._isSuccess = result.success
                    }
                    resultData.push(item)
                })
                this.resultData = resultData
            },
            complied(val, old) {
                if (val !== old && val) {
                    this.loading = false
                    this.$emit('complied', this.importResult)
                    if (this.faildLength === 0) {
                        this.$message.success('导入成功!')
                        this.dialogShow = false
                    }
                }
            }
        },
        computed: {
            importData() {
                return this.data.parseData || []
            },
            rawData() {
                return this.data.raw || []
            },
            processData() {
                let {resultData, rawData} = this
                return resultData.length ? resultData : rawData
            },
            tableHeader() {
                let header = []
                this.rawData.forEach(data => {
                    Object.keys(data).forEach(key => {
                        if (header.indexOf(key) === -1) {
                            header.push(key)
                        }
                    })
                })
                return header
            },
            faildData() {
                return this.importResult.filter(v => v ? !v.success : false)
            },
            successLength() {
                return this.importResult.filter(v => v && v.success).length
            },
            faildLength() {
                return this.faildData.length
            },
            allResultLength() {
                return this.importResult.filter(a => a).length
            },
            complied() {
                let complied = this.allResultLength === this.processData.length
                return complied
            }
        },
        methods: {
            downloadFaildData() {
                const data = this.faildData.map(data => data.data)
                if (data.length) {
                    ExcelUtils.jsonExport(data, {onlyMapping: false})
                }
                this.show = false
            },
            async beginImport() {
                this.loading = true
                this.importResult = new Array(this.importData.length)
                let currentPromise = Promise.resolve()
                const hasSaveApi = this.saveApi && typeof this.saveApi === 'function'
                this.importData.forEach((data, index) => {
                    const saveAction = () => {
                        if (hasSaveApi) {
                            return this.saveApi(data, index).then(() => {
                                this.$set(this.importResult, index, {
                                    success: true,
                                    data: this.rawData[index],
                                    jsonData: data
                                })
                            }).catch(e => {
                                this.$set(this.importResult, index, {
                                    success: false,
                                    data: this.rawData[index],
                                    error: e,
                                    jsonData: data
                                })
                            })
                        } else {
                            this.$set(this.importResult, index, {
                                success: true,
                                data: this.rawData[index],
                                jsonData: data
                            })
                        }
                        return Promise.resolve()
                    }
                    currentPromise = currentPromise.then(() => {
                        return saveAction()
                    })
                })
            }
        }
    }
</script>
<style lang="less" rel="stylesheet/less">
    .import-dialog{
        .import-result-msg{
            font-size: .8em;
            padding: @global-padding/4;
            &.success{
                background: @primary-color;
                color: #fff;
            }
            &.faild{
                background: @danger-color;
                color: #fff;
            }
        }
    }
</style>
