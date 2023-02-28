package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult

object WifiCredentials{
    private lateinit var network: ScanResult
    private var password: String? = null
    private var wifiType: String? = null

    fun setNetwork(network: ScanResult){
        this.network = network
    }

    fun getNetwork():ScanResult{
        return network
    }

    fun getPassword():String{
        return password!!
    }

    fun getWifiType():String{
        return wifiType!!
    }

    fun setPassword(pwd: String){
        this.password = pwd
    }

    fun setWifiType(type: String){
        this.wifiType = type
    }




}
