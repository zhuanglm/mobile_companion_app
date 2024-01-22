package com.esightcorp.mobile.app.networking

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus

// For network-related updates
interface NetworkListener {
    // to update the list of networks
    fun onWifiNetworkFound(result: ScanResult)

    fun onNetworkConnected()

    // Unable to connect screen
    fun onNetworkConnectionError()

    fun onScanFailed()

    fun onNetworkNotFound()
}

// For system status checks and updates
interface SystemStatusListener {
    // check if we're scanning or not
    fun onScanStatusUpdated(status: ScanningStatus)

    // turn on wifi screen
    fun onWifiDisabled()

    fun onWifiEnabled()

    fun onGoWifiDisabled()

    // Remote BLE device disconnected callback
    fun onBluetoothDisconnected()
}

// For errors and issues
interface ErrorListener {
    fun onErrorTest()

    fun onErrorWifiConnectionTimeout()

    fun onErrorWifiInvalidPassword()

    fun onErrorWifiWPALessThan8()

    fun onPlatformError()
}

// For special cases and screens
interface SpecialCaseListener {
    // ConnectedToWifi screen
    fun alreadyConnectedToWifi(status: Boolean)
}
