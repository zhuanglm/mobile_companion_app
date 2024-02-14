/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.state

import java.util.EnumSet


data class HomeUiState(
    val bluetoothState: BluetoothState? = null,
    val connectedDevice: String = "",
) {
    enum class BluetoothState {
        ENABLED,
        DISABLED,
        CONNECTED,
        DISCONNECTED,
    }
}

data class HomePermissionUiState(
    val allPermissionsGranted: Boolean? = null,
    val rationaleReasons: EnumSet<RationaleReason>? = null,
) {
    enum class RationaleReason {
        FOR_BLUETOOTH,
        FOR_LOCATION
    }
}
