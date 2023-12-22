package com.esightcorp.mobile.app.home.repositories


/**
 * Data layer implementation for [HomeScreen]
 */
interface HomeRepositoryListener {
    fun onBluetoothDisabled()
    fun onBluetoothEnabled()
    fun onBluetoothDeviceDisconnected()
}
