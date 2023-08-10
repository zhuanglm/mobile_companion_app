package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus

// For network-related updates
interface NetworkListener {
    // to update the list of networks
    fun onWifiNetworkFound(result: ScanResult): Unit

    fun onNetworkConnected(): Unit

    // Unable to connect screen
    fun onNetworkConnectionError(): Unit

    fun onScanFailed(): Unit

    fun onNetworkNotFound(): Unit
}

// For system status checks and updates
interface SystemStatusListener {
    // check if we're scanning or not
    fun onScanStatusUpdated(status: ScanningStatus): Unit

    // turn on wifi screen
    fun onWifiDisabled(): Unit

    fun onWifiEnabled(): Unit

    fun onGoWifiDisabled(): Unit

    // turn on bluetooth screen & send the user back to the launch screen
    fun onBluetoothDisconnected(): Unit
}

// For errors and issues
interface ErrorListener {
    fun onErrorTest(): Unit

    fun onErrorWifiConnectionTimeout(): Unit

    fun onErrorWifiInvalidPassword(): Unit

    fun onErrorWifiWPALessThan8(): Unit

    fun onPlatformError(): Unit
}

// For special cases and screens
interface SpecialCaseListener {
    // ConnectedToWifi screen
    fun alreadyConnectedToWifi(): Unit
}