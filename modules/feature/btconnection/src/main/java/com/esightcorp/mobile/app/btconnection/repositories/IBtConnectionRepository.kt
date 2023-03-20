package com.esightcorp.mobile.app.btconnection.repositories

import android.bluetooth.BluetoothDevice

interface IBtConnectionRepository {
    fun scanStatus(isScanning: com.esightcorp.mobile.app.utils.ScanningStatus)
    fun deviceListReady(deviceList: MutableList<String>)
    fun onDeviceConnected(device: BluetoothDevice, connected: Boolean)
    fun onBtStateUpdate(enabled: Boolean): Unit
}