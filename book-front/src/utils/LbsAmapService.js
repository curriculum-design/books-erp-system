// 高德接口备注
const Superagent = require('superagent')
const LBS_AMAP_KEY = '9ab1e7eceb8fbbcd85144f5dba033c98'

const callApi = async (url, data) => {
    const {body: response} = await Superagent.get(url, {
        key: LBS_AMAP_KEY,
        ...data})
    if (response.status === '1') {
        return response
    } else {
        console.error('gode api error: ', response)
    }
}

const callApiBacth = async (urls) => {
    const {
        body: response
    } = await Superagent.post('https://restapi.amap.com/v3/batch?key=' + LBS_AMAP_KEY, {
        ops: urls.map(u => {
            return {url: u}
        })
    })
    return response.map(r => r.body)
}

export default {
    async getLaAndLo(address) {
        const { geocodes = [] } = await callApi('https://restapi.amap.com/v3/geocode/geo', {
            address: address,
            batch: true
        })
        return geocodes
    },
    async direction(locations) {
        const opts = locations.map(loc => {
            return `/v3/direction/walking?key=${LBS_AMAP_KEY}&origin=${loc.origin}&destination=${loc.destination}`
        })
        return await callApiBacth(opts)
    }
}
