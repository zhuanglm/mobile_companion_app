/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils.bluetooth

enum class BleConnectionStatus {
    Unknown,
    Connecting,
    Connected,

    //TODO: Unify with the DisconnectUiState::State???
    Disconnecting,
    Disconnected,

    ConnectionFailed,
}
