package com.esightcorp.mobile.app.wificonnection.repositories

import android.net.wifi.ScanResult

interface WifiConnectionRepoListener {
    fun onBluetoothNotConnected(): Unit
    fun onNetworkListUpdated(list: MutableList<ScanResult>): Unit


}