/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.repositories

interface WifiConnectionListener : BleConnectionStatusListener {
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
