/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.state

import com.esightcorp.mobile.app.utils.ScanningStatus

data class BtSearchingUiState(
    val isScanning: ScanningStatus = ScanningStatus.Unknown,
    val isBtEnabled: Boolean? = null,
)

data class BtDevicesUiState(
    val listOfAvailableDevices: List<String> = mutableListOf(),
    val isBtEnabled: Boolean = false,
    val deviceMapCache: List<String> = mutableListOf()
)

data class BtConnectingUiState(
    val isBtEnabled: Boolean = false,
    val didDeviceConnect: Boolean? = null,
    val deviceName: String? = null,
    val deviceAddress: String? = null
)

data class BtConnectedUiState(
    val isBtEnabled: Boolean = true,
    val deviceName: String? = null,
    val deviceAddress: String? = null
)

data class UnableToConnectUiState(
    val isBtEnabled: Boolean = false,
)

data class NoDevicesFoundUiState(
    val isBtEnabled: Boolean = false,
)

data class BtDisabledUiState(
    val isBtEnabled: Boolean = false,
)
