package com.esightcorp.mobile.app.bluetooth

interface EshareBluetoothModelListener {
    fun onEshareReady()

    fun onEshareIpNotReachable()
    fun onEshareAddrNotAvailable()
    fun onEshareBusy()

    fun onEshareStopped()
    fun onUserCancelled()
}
