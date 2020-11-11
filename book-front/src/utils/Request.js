const Es6Promise = require('es6-promise')
const Superagent = require('superagent')
Es6Promise.polyfill()

function objectToQueryString(obj) {
    return Object.keys(obj)
        .filter(key => obj[key] !== null && obj[key] !== '' && encodeURIComponent(obj[key]).trim() !== '')
        .map(key => key + '=' + encodeURIComponent(obj[key]))
        .join('&')
}

function object2KeyValueParnter(obj) {
    if (typeof obj !== 'object') {
        return []
    }
    let params = {}
    Object.keys(obj).forEach(k => {
        let val = obj[k]
        if (val !== '' && val !== undefined && val !== null) {
            if (Array.isArray(val)) {
                val = val.join(',')
            } else if (val instanceof Date) {
                val = val.getTime()
            } else if (typeof val === 'string') {
                val = val.trim()
            }
            params[k] = val
        }
    })
    return params
}

function serializeJson(obj) {
    return JSON.stringify(obj)
}

function serializeForm(obj) {
    return objectToQueryString(object2KeyValueParnter(obj))
}

Superagent.serialize['application/x-www-form-urlencoded'] = serializeForm
Superagent.serialize['application/json'] = serializeJson

export default {
    post(url, params, header) {
        return Superagent.post(url).type('form').send(params).set(header)
    },
    postJson(url, params, header) {
        return Superagent.post(url).send(params).accept('application/json').set(header)
    },
    postPercent(url, params, header, percentCallback) {
        return Superagent.post(url).send(params).set(header).on(`progress`, e => {
            if (typeof percentCallback === 'function') {
                percentCallback(e)
            }
        })
    },
    put(url, params, header) {
        params = serializeForm(params)
        return Superagent.put(`${url}?${params}`).type('json').send().set(header)
    },
    putJson(url, params, header) {
        return Superagent.put(url).send(params).type('application/json').set(header)
    },
    get(url, params, header = {}) {
        let buildGet = Superagent.get(url)
        if (params) {
            buildGet = buildGet.query(objectToQueryString(params))
        }
        return buildGet.set(header)
    },
    del(url, params, header) {
        return Superagent.del(url).query(params).set(header)
    }
}
