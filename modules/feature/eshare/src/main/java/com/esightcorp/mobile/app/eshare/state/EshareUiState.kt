package com.esightcorp.mobile.app.eshare.state

import com.esightcorp.mobile.app.utils.eShareConnectionStatus

data class RadioState(
    val isBtEnabled: Boolean = true,
    val isWifiEnabled: Boolean = false,
)

data class EshareConnectingUiState(
    val radioState: RadioState = RadioState(),
    val connectionState: eShareConnectionStatus = eShareConnectionStatus.Unknown,
)

data class EshareConnectedUiState(
    val radioState: RadioState = RadioState(),
    val connectionState: eShareConnectionStatus = eShareConnectionStatus.Initiated,
)

data class HotspotSetupUiState(
    val radioState: RadioState = RadioState(),
    val networkName: String = "",
    val networkPassword: String = "",
)
