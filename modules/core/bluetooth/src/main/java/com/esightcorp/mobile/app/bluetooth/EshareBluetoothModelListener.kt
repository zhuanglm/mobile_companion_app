package com.esightcorp.mobile.app.bluetooth

interface EshareBluetoothModelListener {
    fun onEshareIpNotReachable()
    fun onEshareAddrNotAvailable()
    fun onEshareBusy()
}