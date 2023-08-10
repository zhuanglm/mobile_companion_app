package com.esightcorp.mobile.app.bluetooth

interface BluetoothConnectionListener {

    fun onDeviceDisconnected()
    fun onDeviceConnected()
    fun onConnectionStateQueried(state: Boolean)
}