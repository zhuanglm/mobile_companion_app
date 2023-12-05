package com.esightcorp.mobile.app.bluetooth

enum class WifiConnectionStatus {
    WIFI_STATUS_NONE,
    WIFI_STATUS_ERROR,

    ;

    companion object {
        fun from(input: String?) = values().find { it.name == input }
    }
}
