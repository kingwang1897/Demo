window.WebSockets = (function () {
    const WebSocketApi = function (builder) {
        let that = this
        this.enableReconnect = true
        this.webSocket = undefined
        this.url = builder.getUrl()
        this.onopen =
            builder.getOnopen() ||
            function () {
                console.log('WebSocket connected.')
            }
        this.onmessage =
            builder.getOnmessage() ||
            function (e) {
                console.log(e.data)
            }
        this.onclose =
            builder.getOnclose() ||
            function () {
                console.log('WebSocket Closed.')
                that.reConnect()
            }
        this.onerror =
            builder.getOnerror() ||
            function (message) {
                console.log(message || 'WebSocket connect failed.')
            }
        window.onbeforeunload = () => {
            console.log('window.onbeforeunload')
            this.close()
        }
    };
    WebSocketApi.prototype = {
        send: function (message) {
            console.log('send..')
            if (this.webSocket && this.webSocket.readyState === 1) {
                this.webSocket.send(JSON.stringify(message))
            } else {
                this.onerror('WebSocket connect failed')
            }
        },
        connect: function () {
            this.enableReconnect = true
            this.webSocket = new WebSocket(this.url)
            this.webSocket.onopen = this.onopen
            this.webSocket.onmessage = this.onmessage
            this.webSocket.onclose = this.onclose
            this.webSocket.onerror = this.onerror
        },
        close: function () {
            this.enableReconnect = false
            this.clear()
            this.webSocket.close()
        },
        clear: function () {
            if (window.timer) {
                clearTimeout(window.timer)
            }
        },
        isOpen: function () {
            return this.webSocket.readyState === 1
        },
        reConnect: function () {
            let that = this
            this.clear()
            if (this.enableReconnect) {
                window.timer = window.setTimeout(() => {
                    if (that.webSocket.readyState === 1) {
                        console.log('WebSocket reconnected.')
                    } else {
                        console.log('WebSocket reconnecting..')
                        that.connect()
                    }
                }, 3000)
            }
        }
    }

    const Builder = function (url) {
        this.url = url
    };
    Builder.prototype = {
        setUrl: function (url) {
            this.url = url
            return this
        },
        onOpen: function (onopen) {
            this.onopen = onopen
            return this
        },
        onMessage: function (onmessage) {
            this.onmessage = onmessage
            return this
        },
        onClose: function (onclose) {
            this.onclose = onclose
            return this
        },
        onError: function (onerror) {
            this.onerror = onerror
            return this
        },
        getOnopen: function () {
            return this.onopen
        },
        getOnmessage: function () {
            return this.onmessage
        },
        getOnclose: function () {
            return this.onclose
        },
        getOnerror: function () {
            return this.onerror
        },
        getUrl: function () {
            return this.url
        },

        build: function () {
            if (!('WebSocket' in window)) {
                throw new Error('your browser does not support webSocket')
            }
            if (!this.url || this.url.trim().length === 0) {
                throw new Error('WebSocket url can not be empty.')
            }
            return new WebSocketApi(this)
        }
    }
    WebSocketApi.Builder = Builder
    WebSocketApi.newBuilder = () => new Builder()
    return WebSocketApi
})();
// export default WebSockets
