module.exports = {
    '/management-system-api': {
        target: 'http://localhost:18200',
        // secure: false,
        changeOrigin: true,
        secure: false,
        pathRewrite: {
            '^/management-system-api/': '/'
        }
    },
}
