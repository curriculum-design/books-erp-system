import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
    routes: [
        {
            path: '/login/:token?',
            name: 'login',
            meta: {auth: false},
            component: require('../views/login')
        },
        {
            path: '/',
            component: () => import('../components/container'),
            children: require('./views-route.js').default
        },
    ]
})

export default router
