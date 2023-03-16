package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import com.juul.kable.State

interface BluetoothModelListener {
    fun listOfDevicesUpdated():Unit
    fun onBatchScanResults(results: List<ScanResult>): Unit
    fun onScanFailed(error: Int): Unit
    fun onScanStarted():Unit
    fun onScanFinished():Unit
    fun onDeviceConnected(device: BluetoothDevice, connected: Boolean): Unit
    fun onBluetoothStateChanged()


}