/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.state

import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository.HotspotCredential
import com.esightcorp.mobile.app.utils.EShareConnectionStatus

data class RadioState(
    val isBtEnabled: Boolean = true,
    val isWifiEnabled: Boolean = true,
)

data class EshareConnectedUiState(
    val radioState: RadioState = RadioState(),
    val connectionState: EShareConnectionStatus = EShareConnectionStatus.Unknown,
    val isDeviceConnected: Boolean = true,
)

data class EshareStoppedUiState(
    val titleId: Int = -1,
    val descriptionId: Int = -1,
    val isDeviceConnected: Boolean = true,
)

data class HotspotSetupUiState(
    val radioState: RadioState = RadioState(),
    val hotspotCredential: HotspotCredential? = null,
    val isDeviceConnected: Boolean = true,
    val hotspotStatus: HotspotStatus? = null,
)
