package com.esightcorp.mobile.app.networking.storage

import android.net.wifi.ScanResult
import android.util.Log
import com.esightcorp.mobile.app.networking.WifiCredentials

object WifiCache {

    private var networkList: MutableList<ScanResult> = mutableListOf()
    val credentials = WifiCredentials
    private var currentFlow: WifiFlow = WifiFlow.NotInUse

    fun getNetworkList(): MutableList<ScanResult> {
        return networkList
    }

    fun selectNetwork(network: ScanResult) {
        Log.i("WifiCache", "selectNetwork: ${network.SSID}")
        WifiCredentials.setNetwork(network)
    }

    fun setWifiFlow(flow: WifiFlow) {
        Log.d("WIFI_CACHE", "setWifiFlow: ${flow.toString()}")
        currentFlow = flow
    }

    fun getWifiFlow(): WifiFlow {
        return currentFlow
    }

    fun finishWifiFlow(){
        currentFlow = WifiFlow.NotInUse
    }

    fun addNetworkToNetworkList(result: ScanResult): Boolean {
        Log.d("WifiCache", "addNetworkToNetworkList: " + result.SSID)
        Log.d("WifiCache", result.toString())
        return if (networkList.none { it.BSSID == result.BSSID }) {
            networkList.add(result)
            true
        } else {
            false
        }
    }

    sealed class WifiFlow() {
        object QrFlow : WifiFlow()
        object BluetoothFlow : WifiFlow()
        object NotInUse : WifiFlow()
    }

}