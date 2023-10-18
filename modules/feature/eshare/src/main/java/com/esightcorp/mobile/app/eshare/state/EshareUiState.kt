package com.esightcorp.mobile.app.eshare.state

import com.esightcorp.mobile.app.utils.eShareConnectionStatus

data class RadioState(
    val isBtEnabled: Boolean = true,
    val isWifiEnabled: Boolean = true,
)

data class DeviceConnectionState(
    val isDeviceConnected: Boolean = true
)

data class EshareConnectedUiState(
    val radioState: RadioState = RadioState(),
    val connectionState: eShareConnectionStatus = eShareConnectionStatus.Unknown,
    val deviceConnectionState: DeviceConnectionState = DeviceConnectionState(),
)

data class HotspotSetupUiState(
    val radioState: RadioState = RadioState(),
    val networkName: String = "",
    val networkPassword: String = "",
    val isDeviceConnected: DeviceConnectionState = DeviceConnectionState(),
)
