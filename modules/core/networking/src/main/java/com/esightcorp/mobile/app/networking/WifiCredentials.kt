/*
 * LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import android.util.Log
import com.esightcorp.mobile.app.ui.R

object WifiCredentials {
    private var password: String = ""
    private var wifiType: Int = R.string.kWifiSecurityTypeWPA
    private var ssid: String? = null

    private fun getSSID(network: ScanResult?): String? {
        if(network != null){
            Log.d("WifiCredentials", "getSSID: ${network.ssidName()}")
        }
        return network?.ssidName()?.removePrefix("\"")?.removeSuffix("\"")
    }

    fun setNetwork(network: ScanResult) {
        this.ssid = getSSID(network)
    }

    fun setNetwork(ssid: String, securityType: Int, password: String?) {
        this.ssid = ssid
        this.wifiType = securityType
        this.password = password?:""
    }
    fun getSSID(): String? {
        return ssid
    }

    fun getPassword(): String {
        return password
    }

    fun getWifiType(): Int {
        return wifiType
    }

    fun setPassword(pwd: String) {
        this.password = pwd
    }

    fun setWifiType(type: Int) {
        this.wifiType = type
    }
}
