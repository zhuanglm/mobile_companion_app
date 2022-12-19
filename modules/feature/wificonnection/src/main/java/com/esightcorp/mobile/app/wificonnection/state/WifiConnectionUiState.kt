package com.esightcorp.mobile.app.wificonnection.state

import android.net.wifi.ScanResult


data class WifiConnectionUiState(
    val isWifiEnabled: Boolean = false,
    val arePermissionsGranted: Boolean = false,
    val ssid: String = "",
    val password: String = "",
    val passwordSubmitted: Boolean = false,
    val wifiType: String ="WPA/WPA2",
    val wifiTypeSubmitted: Boolean = false,
    val qrCodeButtonVisibility: Boolean = false,
    val currentSelectedNetwork: ScanResult? = null,
    val networkList: List<ScanResult> = mutableListOf())
