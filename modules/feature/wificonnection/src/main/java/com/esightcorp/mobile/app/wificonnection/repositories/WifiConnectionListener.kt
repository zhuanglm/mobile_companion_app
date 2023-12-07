package com.esightcorp.mobile.app.wificonnection.repositories

interface WifiConnectionListener {
    /**
     * If the mobile phone has bluetooth enabled or not
     */
    fun onBluetoothStatusUpdate(status: Boolean) {}

    /**
     * If the mobile phone has wifi enabled or not
     */
    fun onWifiStatusUpdate(status: Boolean) {}

    /**
     * When we connect to a network, we call this method
     * If the connection was successful, success will be true
     * If the connection was unsuccessful, success will be false
     */
    fun onWifiConnected(success: Boolean) {}

    /**
     * Error methods are all below, and should be handled accordingly
     */
    fun onWifiNetworkNotFound() {}
    fun onWifiConnectionTimeout() {}
    fun onWifiInvalidPassword() {}
    fun onWifiWPALessThan8() {}
    fun onWifiConnectionTest() {}
    fun onPlatformError() {}
    fun onGoWifiDisabled() {}
    fun onNetworkConnectionError() {}
}
