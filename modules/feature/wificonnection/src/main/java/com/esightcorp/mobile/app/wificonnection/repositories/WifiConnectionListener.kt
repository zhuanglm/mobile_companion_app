package com.esightcorp.mobile.app.wificonnection.repositories

interface WifiConnectionListener {
    /*
    If the mobile phone has bluetooth enabled or not
    */
    fun onBluetoothStatusUpdate(status: Boolean): Unit

    /*
    If the mobile phone has wifi enabled or not
     */
    fun onWifiStatusUpdate(status: Boolean): Unit

    /*
    When we connect to a network, we call this method
    If the connection was successful, success will be true
    If the connection was unsuccessful, success will be false
     */
    fun onWifiConnected(success: Boolean): Unit

    /*
    Error methods are all below, and should be handled accordingly
     */
    fun onWifiNetworkNotFound(): Unit
    fun onWifiConnectionTimeout(): Unit
    fun onWifiInvalidPassword(): Unit
    fun onWifiWPALessThan8(): Unit
    fun onWifiConnectionTest(): Unit
    fun onPlatformError(): Unit
    fun onGoWifiDisabled():Unit

}