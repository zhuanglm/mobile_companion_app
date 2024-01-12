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
