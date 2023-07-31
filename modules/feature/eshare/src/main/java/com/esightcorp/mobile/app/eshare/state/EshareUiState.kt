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
