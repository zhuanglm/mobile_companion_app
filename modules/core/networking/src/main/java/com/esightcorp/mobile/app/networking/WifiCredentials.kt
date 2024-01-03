package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import android.util.Log

object WifiCredentials {

    private var network: ScanResult? = null
    private var password: String = ""
    private var wifiType: String = "WPA/WPA2"

    fun getSSID(): String? {
        if(network != null){
            Log.d("WifiCredentials", "getSSID: ${network!!.ssidName()}")
        }
        return network?.ssidName()?.removePrefix("\"")?.removeSuffix("\"")
    }

    fun setNetwork(network: ScanResult) {
        this.network = network
    }

    fun getPassword(): String {
        return password
    }

    fun getWifiType(): String {
        return wifiType
    }

    fun setPassword(pwd: String) {
        this.password = pwd
    }

    fun setWifiType(type: String) {
        this.wifiType = type
    }
}
