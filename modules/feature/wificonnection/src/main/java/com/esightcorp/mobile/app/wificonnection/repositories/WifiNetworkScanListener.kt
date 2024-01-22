package com.esightcorp.mobile.app.wificonnection.repositories

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus

interface WifiNetworkScanListener {
    /*
    If the mobile phone has bluetooth enabled or not
    */
    fun onBluetoothStatusUpdate(status: Boolean)

    /*
    If the mobile phone has wifi enabled or not
    */
    fun onWifiStatusUpdate(status: Boolean)

    /*
    Dumps a whole list of networks to the UI
    */
    fun onNetworkListUpdated(list: MutableList<ScanResult>)

    /*
    When we start or stop scanning, we call this method
    Will be one of the ScanningStatus enum values
    */
    fun onScanStatusUpdated(status: ScanningStatus)

    /**
     * Glasses wifi connection status
     */
    fun onWifiAlreadyConnected(status: Boolean)

}