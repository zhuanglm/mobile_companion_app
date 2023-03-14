package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus


interface WifiModelListener {
    //to update the list of networks
    fun onWifiNetworkFound(result: ScanResult): Unit
    //check if were scanning or not
    fun onScanStatusUpdated(status: ScanningStatus): Unit
    fun onNetworkConnected(): Unit
    //Unable to connect screen
    fun onNetworkConnectionError(): Unit
    fun onScanFailed():Unit
    //turn on wifi screen
    fun onWifiDisabled():Unit
    //turn on bluetooth screen & send the user back to the launch screen
    fun onBluetoothDisconnected():Unit
    //ConnectedToWifi screen
    fun AlreadyConnectedToWifi():Unit

    fun onErrorTest():Unit
    fun onErrorWifiConnectionTimeout():Unit
    fun onErrorWifiInvalidPassword():Unit
    fun onErrorWifiWPALessThan8():Unit

}