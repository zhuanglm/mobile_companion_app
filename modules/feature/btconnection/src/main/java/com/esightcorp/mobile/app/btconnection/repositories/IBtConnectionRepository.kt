package com.esightcorp.mobile.app.btconnection.repositories

import android.bluetooth.BluetoothDevice

interface IBtConnectionRepository {
    fun scanStatus(isScanning: ScanningStatus)
    fun deviceListReady(deviceList: MutableList<String>)
    fun onDeviceConnected(device: BluetoothDevice)
}