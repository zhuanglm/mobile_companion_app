package com.esightcorp.mobile.app.networking.storage

object eShareCache {

    private var port: Int = -1
    private var ipAddress: String = ""

    fun setPort(port: Int) {
        this.port = port
    }

    fun setIpAddress(ipAddress: String) {
        this.ipAddress = ipAddress
    }

    fun getPort(): Int {
        return port
    }

    fun getIpAddress(): String {
        return ipAddress
    }

    fun clear() {
        port = -1
        ipAddress = ""
    }
}