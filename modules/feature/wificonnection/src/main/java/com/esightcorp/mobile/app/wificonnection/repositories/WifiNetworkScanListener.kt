/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.repositories

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus

interface WifiNetworkScanListener : BleConnectionStatusListener {
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
