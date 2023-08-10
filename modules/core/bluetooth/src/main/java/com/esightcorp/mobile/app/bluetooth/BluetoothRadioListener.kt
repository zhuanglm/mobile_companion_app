package com.esightcorp.mobile.app.bluetooth

interface BluetoothRadioListener {
    fun onBluetoothEnabled()
    fun onBluetoothDisabled()
    fun onBluetoothStateQueried(state: Boolean)

}