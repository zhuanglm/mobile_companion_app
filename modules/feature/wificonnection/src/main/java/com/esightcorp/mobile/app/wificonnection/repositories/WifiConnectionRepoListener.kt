package com.esightcorp.mobile.app.wificonnection.repositories

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus

interface WifiConnectionRepoListener {
    fun onBluetoothStatusUpdate(status: Boolean): Unit
    fun onNetworkListUpdated(list: MutableList<ScanResult>): Unit
    fun onScanStatusUpdated(status: ScanningStatus): Unit
    fun onWifiConnected(success: Boolean): Unit


}