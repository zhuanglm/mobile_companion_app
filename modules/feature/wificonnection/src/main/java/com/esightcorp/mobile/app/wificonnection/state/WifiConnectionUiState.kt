package com.esightcorp.mobile.app.wificonnection.state


data class WifiConnectionUiState(
    val isWifiEnabled: Boolean = false,
    val arePermissionsGranted: Boolean = false,
    val ssid: String = "",
    val password: String = "",
    val wifiType: String =""
)