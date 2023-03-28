package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import android.util.Log

object WifiCache {

    private var networkList: MutableList<ScanResult> = mutableListOf()
    val credentials = WifiCredentials
    private var currentFlow: WifiFlow = WifiFlow.NotInUse

    fun getNetworkList(): MutableList<ScanResult> {
        return networkList
    }
// TODO: Can we remove this
//    fun setNetworkList(list: MutableList<ScanResult>) {
//        this.networkList = list
//    }

    fun selectNetwork(network: ScanResult) {
        Log.i("WifiCache", "selectNetwork: ${network.SSID}")
        this.credentials.setNetwork(network)
    }
//TODO: Can we remove this
//    fun enterNetworkPassword(pwd: String) {
//        this.credentials.setPassword(pwd)
//    }

    fun setWifiFlow(flow: WifiFlow) {
        this.currentFlow = flow
    }

    fun getWifiFlow():WifiFlow{
        return this.currentFlow
    }

    fun finishWifiFlow(){
        this.currentFlow = WifiFlow.NotInUse
    }

    fun addNetworkToNetworkList(result: ScanResult): Boolean {
        Log.d("WifiCache", "addNetworkToNetworkList: " + result.SSID)
        Log.d("WifiCache", result.toString())
        return if (networkList.none { it.BSSID == result.BSSID }) {
            this.networkList.add(result)
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