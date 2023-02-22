package com.esightcorp.mobile.app.wificonnection.repositories

import android.net.wifi.ScanResult

object WifiCredentials{
    private lateinit var network: ScanResult
    private lateinit var password: String
    private lateinit var wifiType: String

    fun setNetwork(network: ScanResult){
        this.network = network
    }

    fun getNetwork():ScanResult{
        return network
    }


}
