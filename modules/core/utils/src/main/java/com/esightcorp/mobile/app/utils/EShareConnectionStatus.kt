package com.esightcorp.mobile.app.utils

enum class EShareConnectionStatus {
    Unknown,

    Connected,
    Disconnected,
    ReceivedUserRejection,
    Timeout,
    AddressNotAvailable,
    IpNotReachable,
    Busy,
    RequireSetupWifi,
}
