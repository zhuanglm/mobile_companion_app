package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.le.ScanResult

interface BluetoothModelListener : BluetoothRadioListener, BluetoothConnectionListener {
    fun listOfDevicesUpdated() {}
    fun onBatchScanResults(results: List<ScanResult>) {}
    fun onScanFailed(error: Int) {}
    fun onScanStarted() {}
    fun onScanFinished() {}
    fun onScanCancelled() {}
}
