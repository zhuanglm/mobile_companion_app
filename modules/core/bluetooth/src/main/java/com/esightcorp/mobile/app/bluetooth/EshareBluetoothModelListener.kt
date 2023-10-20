package com.esightcorp.mobile.app.bluetooth

interface EshareBluetoothModelListener {
    fun onEshareIpNotReachable()
    fun onEshareAddrNotAvailable()
    fun onEshareBusy()
    fun onEshareReady()

    fun onEshareStopped()
    fun onUserCancelled()
}