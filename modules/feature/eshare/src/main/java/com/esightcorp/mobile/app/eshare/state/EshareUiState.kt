package com.esightcorp.mobile.app.eshare.state

import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.utils.EShareConnectionStatus

data class RadioState(
    val isBtEnabled: Boolean = true,
    val isWifiEnabled: Boolean = true,
)

data class DeviceConnectionState(
    val isDeviceConnected: Boolean = true
)

data class EshareConnectedUiState(
    val radioState: RadioState = RadioState(),
    val connectionState: EShareConnectionStatus = EShareConnectionStatus.Unknown,
    val deviceConnectionState: DeviceConnectionState = DeviceConnectionState(),
)

data class EshareStoppedUiState(
    val titleId: Int = -1,
    val descriptionId: Int = -1,
)

data class HotspotSetupUiState(
    val radioState: RadioState = RadioState(),
    val networkName: String = "",
    val networkPassword: String = "",
    val isDeviceConnected: DeviceConnectionState = DeviceConnectionState(),
    val hotspotStatus: HotspotStatus? = null,
)
