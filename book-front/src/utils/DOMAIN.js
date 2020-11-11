/**
 * Created by linlx on 2016/4/27.
 */

const origin = `${location.protocol}//${location.hostname}`
const SAPI_DOMAIN_PRO = `${origin}/sapi`
const SAPI_DOMAIN_DEV = `/sapi`

const IS_LOCAL_DEV = (location.port && location.port !== '80') || location.host.indexOf('localhost') === 0
const IS_DEV = location.host.indexOf('dev.') === 0
const IS_TEST = location.host.indexOf('test.') === 0 || location.host.indexOf('192') === 0
const IS_PRO = !IS_LOCAL_DEV && !IS_DEV && !IS_TEST

const M_HOST_MAP = {
    local: 'dev.m.shanhs.com.cn',
    dev: 'dev.m.shanhs.com.cn',
    test: 'test.m.shanhs.com.cn',
    pro: 'm.shanhs.com',
}

const BOSS_HOST_MAP = {
    local: 'dev.www.shanhs.com.cn',
    dev: 'dev.www.shanhs.com.cn',
    test: 'test.www.shanhs.com.cn',
    pro: 'newboss.shanhs.com',
}
const ENV = IS_LOCAL_DEV ? 'local' : (IS_DEV ? 'dev' : (IS_TEST ? 'test' : (IS_PRO ? 'pro' : undefined)))
export default {
    DOMAIN: IS_LOCAL_DEV ? SAPI_DOMAIN_DEV : SAPI_DOMAIN_PRO,
    IS_LOCAL_DEV,
    IS_DEV,
    IS_TEST,
    IS_PRO,
    ENV,
    M_HOST: M_HOST_MAP[ENV],
    BOSS_HOST: BOSS_HOST_MAP[ENV]
}
