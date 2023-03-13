package com.esightcorp.mobile.app.wificonnection.repositories

import android.net.wifi.ScanResult

interface WifiConnectionRepoListener {
    fun onBluetoothStatusUpdate(status: Boolean): Unit
    fun onNetworkListUpdated(list: MutableList<ScanResult>): Unit


}