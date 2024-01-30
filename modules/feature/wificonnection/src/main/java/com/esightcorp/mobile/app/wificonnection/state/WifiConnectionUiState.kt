/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.state

import android.net.wifi.ScanResult
import com.esightcorp.mobile.app.networking.WifiType
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.utils.ScanningStatus

enum class WifiConnectionStatus {
    CONNECTED,
    DISCONNECTED,
    UNKNOWN
}

data class WifiSearchingUiState(
    val isWifiEnabled: Boolean = false,
    val isWifiConnected: Boolean = false,
    val wifiConnectionStatus: WifiConnectionStatus = WifiConnectionStatus.UNKNOWN,
    val scanningStatus: ScanningStatus = ScanningStatus.Unknown,
    val isWifiConnectedAlreadyOnGlasses: Boolean = false,
)

data class SelectNetworkUiState(
    val isWifiEnabled: Boolean = false,
    val networkList: List<ScanResult> = mutableListOf(),
)

data class WifiCredentialsUiState(
    val isWifiEnabled: Boolean = false,
    val password: String = "",
    val isPasswordValid: Boolean = false,
    val passwordSubmitted: Boolean = false,
    val wifiType: WifiType = WifiType.WPA,
    val wifiTypeSubmitted: Boolean = false,
    val wifiFlow: WifiCache.WifiFlow = WifiCache.WifiFlow.NotInUse,
)

data class WifiConnectingUiState(
    val isWifiEnabled: Boolean = false,
    val connectionWasSuccess: Boolean = false,
    val connectionError: Boolean = false,
    val ssid: String = "",
)

data class WifiConnectedUiState(
    val isWifiEnabled: Boolean = false,
    val ssid: String = "",
)

data class WifiConnectionErrorUiState(
    val isWifiEnabled: Boolean = false,
    val error: String = "",
)

data class AlreadyConnectedUiState(
    val isWifiEnabled: Boolean = false,
    val ssid: String = "",
)

data class UnableToConnectUiState(
    val isWifiEnabled: Boolean = false,
    val ssid: String = "",
)

data class WifiOffUiState(
    val isWifiEnabled: Boolean = false,
)

data class WifiAdvancedSettingsUiState(
    val isWifiEnabled: Boolean = false,
    val ssid: String = "",
    val password: String = "",
    val isPasswordValid: Boolean = false,
    val wifiType: WifiType = WifiType.WPA,
    val wifiTypeSubmitted: Boolean = false,
    val passwordSubmitted: Boolean = false,
    val ssidSubmitted: Boolean = false,
    val wifiFlow: WifiCache.WifiFlow = WifiCache.WifiFlow.NotInUse,
)

data class WifiTypeUiState(
    val isWifiEnabled: Boolean = false,
    val wifiType: WifiType = WifiType.WPA,
    val wifiTypeSubmitted: Boolean = false,
)

data class WifiQrCodeUiState(
    val qrString: String = "",
)