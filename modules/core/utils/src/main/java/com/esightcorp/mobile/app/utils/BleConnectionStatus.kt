package com.esightcorp.mobile.app.utils

enum class BleConnectionStatus {
    Unknown,
    Connecting,
    Connected,

    //TODO: Unify with the DisconnectUiState::State???
    Disconnecting,
    Disconnected,

    ConnectionFailed,
}
