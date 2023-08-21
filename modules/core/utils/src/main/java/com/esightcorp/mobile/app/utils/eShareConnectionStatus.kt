package com.esightcorp.mobile.app.utils

sealed class eShareConnectionStatus {
    object Connected : eShareConnectionStatus()
    object Disconnected : eShareConnectionStatus()
    object Unknown : eShareConnectionStatus()
    object Failed: eShareConnectionStatus()
    object Initiated: eShareConnectionStatus()
    object ReceivedUserRejection: eShareConnectionStatus()
    object Timeout: eShareConnectionStatus()
    object ADDR_NOT_AVAILABLE: eShareConnectionStatus()
    object IP_NOT_REACHABLE: eShareConnectionStatus()
    object BUSY: eShareConnectionStatus()


}