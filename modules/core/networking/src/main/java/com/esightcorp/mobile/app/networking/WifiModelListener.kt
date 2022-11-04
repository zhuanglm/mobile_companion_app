package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult


interface WifiModelListener {
    fun onWifiNetworkFound(result: ScanResult): Unit
    fun onNetworkConnected(): Unit
    fun onScanFailed():Unit

}