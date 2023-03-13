package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus


interface WifiModelListener {
    fun onWifiNetworkFound(result: ScanResult): Unit
    fun onScanStatusUpdated(status: ScanningStatus)
    fun onNetworkConnected(): Unit
    fun onScanFailed():Unit
}