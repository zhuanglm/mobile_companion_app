/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.storage

import android.net.wifi.ScanResult
import android.util.Log
import com.esightcorp.mobile.app.networking.WifiCredentials
import com.esightcorp.mobile.app.networking.ssidName

object WifiCache {
    private val _tag = this.javaClass.simpleName

    enum class WifiFlow {
        QrFlow,
        BluetoothFlow,
        NotInUse,
    }

    private val networkList: MutableList<ScanResult> = mutableListOf()
    val credentials = WifiCredentials
    private var currentFlow: WifiFlow = WifiFlow.NotInUse

    @Synchronized
    fun getNetworkList() = networkList

    fun selectNetwork(network: ScanResult) {
        credentials.setNetwork(network)
    }

    @Synchronized
    fun setWifiFlow(flow: WifiFlow) {
        currentFlow = flow
    }

    @Synchronized
    fun getWifiFlow() = currentFlow

    @Synchronized
    fun addNetworkToNetworkList(result: ScanResult): Boolean {
        return if (networkList.none { it.BSSID == result.BSSID || (it.ssidName() == result.ssidName()) }) {
            networkList.add(result)
            Log.d(_tag, "addNetworkToNetworkList: $result")

            true
        } else {
            false
        }
    }
}
