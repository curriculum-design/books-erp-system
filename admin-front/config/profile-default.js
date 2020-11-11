module.exports = {
    '/management-system-api': {
        target: 'https://admin.tulading.tech/api',
        // secure: false,
        changeOrigin: true,
        secure: false,
        pathRewrite: {
            '^/management-system-api/': '/'
        }
    },
}
