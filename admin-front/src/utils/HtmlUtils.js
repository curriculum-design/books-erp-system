export default {
    isIOS() {
        const {userAgent} = navigator
        return userAgent.indexOf('iPhone') > -1 || userAgent.indexOf('iPad') > -1
    },
    setTitle(title) {
        document.title = title
        if (this.isIOS()) {
            const i = document.createElement('iframe')
            i.src = '/favicon.ico'
            i.style.display = 'none'
            i.onload = function() {
                setTimeout(function() {
                    i.remove()
                })
            }
            document.body.appendChild(i)
        }
    },
    isMobile() {
        return window.navigator.userAgent.match(/(phone|pod|iPhone|iPod|Android|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
    }
}
