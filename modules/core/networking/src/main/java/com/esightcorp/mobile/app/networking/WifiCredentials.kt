/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import android.text.TextUtils

object WifiCredentials {
    private var password: String = ""
    private var wifiType = WifiType.WPA
    private var ssid: String? = ""

    private fun getSecurityType(scanResult: ScanResult): WifiType {
        val capabilities = scanResult.capabilities

        return when {
            capabilities.contains("WPA3") -> WifiType.WPA
            capabilities.contains("WPA2") -> WifiType.WPA
            capabilities.contains("WPA") -> WifiType.WPA
            capabilities.contains("WEP") -> WifiType.WEP
            else -> WifiType.NONE // No security
        }
    }

    fun setNetwork(network: ScanResult) {
        this.ssid = network.ssidName()
        this.wifiType = getSecurityType(network)
    }

    fun setNetwork(ssid: String, securityType: WifiType, password: String? = null) {
        this.ssid = ssid
        this.wifiType = securityType

        this.password = when (securityType) {
            WifiType.NONE -> ""
            else -> password ?: ""
        }
    }

    fun clear() = setNetwork("", WifiType.WPA)

    fun getSSID(): String? {
        return ssid
    }

    fun getPassword(): String {
        return password
    }

    fun getWifiType(): WifiType {
        return wifiType
    }

    fun setPassword(pwd: String) {
        this.password = pwd
    }

    fun setWifiType(type: WifiType) {
        this.wifiType = type
    }

    fun isValid(): Boolean {
        val validSSID = !TextUtils.isEmpty(ssid)

        return when (wifiType) {
            WifiType.NONE -> validSSID
            else -> validSSID && !TextUtils.isEmpty(password)
        }
    }
}
