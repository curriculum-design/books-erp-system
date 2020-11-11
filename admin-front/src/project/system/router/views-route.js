const views = [
    {
        path: '/404',
        name: '404',
        meta: {auth: false},
        component: () => import('../views/404')
    },
    {
        path: '/502',
        name: '502',
        meta: {auth: false},
        component: () => import('../views/502')
    }
]
export default views

views.push({
    path: '/sys-admin',
    name: 'sys-admin',
    component: require('../views/sys-admin')
})

views.push({
    path: '/base-book',
    name: 'base-book',
    component: require('../views/base-book')
})

views.push({
    path: '/base-book-type',
    name: 'base-book-type',
    component: require('../views/base-book-type')
})

views.push({
    path: '/base-publisher',
    name: 'base-publisher',
    component: require('../views/base-publisher')
})
