import FileSaver from 'file-saver'
import JSZip from 'jszip'
export default {
    getBlob(src) {
        return new Promise(function(resolve, reject) {
            try {
                const img = new Image()
                img.onload = () => {
                    try {
                        const canvas = document.createElement('canvas')
                        const ctx = canvas.getContext('2d')
                        canvas.width = img.width
                        canvas.height = img.height
                        ctx.drawImage(img, 0, 0)
                        canvas.toBlob(blob => {
                            resolve(blob)
                        })
                    } catch (e) {
                        reject(e)
                    }
                }
                img.onerror = (e) => {
                    reject(e)
                }
                img.crossOrigin = '*'
                img.src = src + '?v=downfile'
            } catch (e) {
                reject(e)
            }
        })
    },
    downFromSrc(src, filename) {
        this.getBlob(src).then(blob => {
            FileSaver.saveAs(blob, filename)
        })
    },
    async downFromSrcBatch(filelist = [], zipName = 'down-file-' + Date.now()) {
        const zip = new JSZip()
        for (let i = 0; i < filelist.length; i++) {
            let {src, filename} = filelist[i]
            try {
                let blob = await this.getBlob(src)
                zip.file(filename, blob)
            } catch (e) {
                console.log(e)
            }
        }
        const content = await zip.generateAsync({type: 'blob'})
        FileSaver.saveAs(content, zipName)
    }
}
