package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.BluetoothDevice

interface BluetoothConnectionListener {
    fun onDeviceDisconnected(device: BluetoothDevice)
    fun onDeviceConnected(device: BluetoothDevice)
}
