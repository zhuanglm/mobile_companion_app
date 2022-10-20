package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult

interface IBluetoothModel {
    fun isBluetoothCurrentlyConnected(): Boolean
    fun getPairedDevicesAndConnectionStatus(): HashMap<BluetoothDevice, Boolean>
    fun onBleDeviceFound(result: ScanResult): Unit
    fun onBatchScanResults(results: List<ScanResult>): Unit
    fun onScanFailed(error: Int): Unit
    fun onScanStarted():Unit
    fun onScanFinished():Unit



}