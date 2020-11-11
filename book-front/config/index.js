// see http://vuejs-templates.github.io/webpack for documentation.
var path = require('path')
var commond = require('./commond')
var projectName = commond.projectName
var url = require('url')
const {profile = 'default'} = commond.config
var proxy = require('./profile-' + profile)
console.log('current profile = ' + profile)
module.exports = {
  projectName: commond.projectName,
  projectDir: commond.projectDir,
  themePath: path.resolve(__dirname, '../src/assets/less/theme.less'),
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, `../dist/${projectName}/index.html`),
    assetsRoot: path.resolve(__dirname, `../dist/${projectName}`),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/management/' + projectName + '/',
    productionSourceMap: false,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: require('./dev.env'),
    port: commond.config.port || 8080,
    autoOpenBrowser: true,
    assetsSubDirectory: 'management/static',
    assetsPublicPath: '/',
    proxyTable: {...proxy,
        // '/web-socket': {
        //     target: 'https://dev.www.shanhs.com.cn',
        //     secure: false,
        //     changeOrigin: true,
        //     ws: true
        // }
    },
    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false
  }
}
