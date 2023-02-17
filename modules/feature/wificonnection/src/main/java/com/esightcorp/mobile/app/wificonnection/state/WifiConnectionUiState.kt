package com.esightcorp.mobile.app.wificonnection.state

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.utils.ScanningStatus


data class WifiConnectionUiState(
    val isWifiEnabled: Boolean = false,
    val arePermissionsGranted: Boolean = false,
    val ssid: String = "",
    val qrCodeButtonVisibility: Boolean = false,
    val currentSelectedNetwork: ScanResult? = null,
    val networkList: List<ScanResult> = mutableListOf(),
    val bluetoothConnected: Boolean = false)

data class WifiSearchingUiState(
    val scanningStatus: ScanningStatus = ScanningStatus.Unknown
)

data class SelectNetworkUiState(
    val networkList: List<ScanResult> = mutableListOf()
)

data class WifiCredentialsUiState(
    val password: String = "",
    val passwordSubmitted: Boolean = false,
    val wifiType: String ="WPA/WPA2",
    val wifiTypeSubmitted: Boolean = false,
)