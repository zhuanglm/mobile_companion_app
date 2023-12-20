package com.esightcorp.mobile.app.btconnection.repositories

import android.bluetooth.BluetoothDevice
import com.esightcorp.mobile.app.utils.ScanningStatus

interface BluetoothConnectionRepositoryCallback {
    fun scanStatus(isScanning: ScanningStatus) {}
    fun deviceListReady(deviceList: MutableList<String>) {}
    fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {}
    fun onBtStateUpdate(enabled: Boolean) {}
}
