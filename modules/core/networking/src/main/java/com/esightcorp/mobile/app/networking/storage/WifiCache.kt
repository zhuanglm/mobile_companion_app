package com.esightcorp.mobile.app.networking.storage

import android.net.wifi.ScanResult
import android.util.Log
import com.esightcorp.mobile.app.networking.WifiCredentials

object WifiCache {
    private val _tag = this.javaClass.simpleName

    enum class WifiFlow {
        QrFlow,
        BluetoothFlow,
        NotInUse,
    }

    private var networkList: MutableList<ScanResult> = mutableListOf()
    val credentials = WifiCredentials
    private var currentFlow: WifiFlow = WifiFlow.NotInUse

    fun getNetworkList() = networkList

    fun selectNetwork(network: ScanResult) {
        credentials.setNetwork(network)
    }

    fun setWifiFlow(flow: WifiFlow) {
        currentFlow = flow
    }

    fun getWifiFlow() = currentFlow

    fun addNetworkToNetworkList(result: ScanResult): Boolean {
        Log.d(_tag, "addNetworkToNetworkList: $result")
        return if (networkList.none { it.BSSID == result.BSSID }) {
            networkList.add(result)
            true
        } else {
            false
        }
    }
}
