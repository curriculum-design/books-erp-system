/**
 * 需要生成的内容包括
 * 1. 路由信息
 * 2. views
 *  a. views列表
 *  b. views表单
 * 3. servic信息
 */
const ejs = require('ejs')
const fs = require('fs')
const Superagent = require('superagent')
const _path = require('path')
function resolve(dir) {
    return _path.join(__dirname, dir)
}

function toCamelCase(source, split = '_') {
    let array = source.split(split)
    let output = array.map(m => m.substr(0, 1).toUpperCase() + m.substr(1)).join('')
    return output
}

function firstLower(source) {
    return source.substr(0, 1).toLowerCase() + source.substr(1)
}

function createDir(filePath) {
    console.log('file: ', filePath)
    let lastIndexOf = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'))
    let dir = filePath.substr(0, lastIndexOf)
    try {
        fs.accessSync(dir, fs.F_OK)
    } catch (e) {
        console.log('创建文件夹--' + dir)
        fs.mkdirSync(dir)
        console.log('创建文件夹' + dir)
    }
}
function writeFile(path, content) {
    path = resolve(path)
    try {
        fs.accessSync(path, fs.F_OK)
        console.log('文件已存在', path)
    } catch (e) {
        try {
            createDir(path)
            fs.writeFileSync(path, content)
            console.log('成功写入文件', path)
        } catch (e) {
            console.log(e)
            console.log('写入文件失败', path)
        }
    }
}

function appendToFile(filepath, content) {
    filepath = resolve(filepath)
    let filecontent = fs.readFileSync(filepath, 'utf-8')
    if (filecontent.indexOf(content) > -1) {
        console.log(filepath, '已存在该内容')
    } else {
        fs.appendFileSync(filepath, content)
        console.log('追加文件内容成功', content)
    }
}

function gen(table) {
    table = table.replace(/-/g, '_')
    const apiName = table.replace(/_/g, '-')
    const serviceName = toCamelCase(table) + 'Service'
    const serviceNameFirstLower = firstLower(serviceName)
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0'
    Superagent.get('http://localhost:18200/common/table-fields').query({table})
    .then(({body: {data: {fields, primaryKey}}}) => {
        const opts = {
            apiName,
            table,
            serviceName,
            primaryKey,
            fields,
            serviceNameFirstLower,
        }
        Object.keys(fields).forEach(k => {
            fields[k] = fields[k] || k
            //
        })
        const serviceTemp = fs.readFileSync(resolve('./service-template.ejs'), 'utf-8')
        let serviceRender = ejs.render(serviceTemp, opts)
        writeFile(`../service/${serviceName}.js`, serviceRender)
        appendToFile('../service/index.js',
        `Vue.prototype.$${serviceNameFirstLower} = require('./${serviceName}.js').default\n`)
        const viewTemp = fs.readFileSync(resolve('./index.vue.ejs'), 'utf-8')
        let viewRender = ejs.render(viewTemp, opts)
        writeFile(`../views/${apiName}/index.vue`, viewRender)
        appendToFile('../router/views-route.js', `
views.push({
    path: '/${apiName}',
    name: '${apiName}',
    component: require('../views/${apiName}')
})\n`)
        const formTemp = fs.readFileSync(resolve('./form.vue.ejs'), 'utf-8')
        let formRender = ejs.render(formTemp, opts)
        writeFile(`../views/${apiName}/form.vue`, formRender)
    }).catch(e => {
        console.log(e)
    })
}
let params = process.argv.slice(2)
params.forEach(gen)
