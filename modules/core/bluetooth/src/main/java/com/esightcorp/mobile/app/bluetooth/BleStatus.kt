package com.esightcorp.mobile.app.bluetooth

enum class WifiConnectionStatus {
    WIFI_STATUS_NONE,
    WIFI_STATUS_ERROR,

    ;

    companion object {
        fun from(input: String?) = values().find { it.name == input }
    }
}

enum class HotspotStatus(val status: String) {
    ENABLED("ENABLED"),
    DISABLED("DISABLED"),
    WAITING("WAITING"),
    ERROR("ERROR"),

    ;

    companion object {
        fun from(value: String?) = values().find { it.status == value }
    }
}

enum class EShareStatus(val code: String) {
    READY("READY"),
    STOPPED("STOPPED"),
    RUNNING("RUNNING"),

    ;

    companion object {
        fun from(value: String?) = values().find { it.code == value }
    }
}
