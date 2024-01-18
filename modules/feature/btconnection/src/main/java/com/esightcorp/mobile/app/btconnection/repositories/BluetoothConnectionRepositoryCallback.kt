package com.esightcorp.mobile.app.btconnection.repositories

import android.bluetooth.BluetoothDevice
import com.esightcorp.mobile.app.utils.ScanningStatus

interface BluetoothConnectionRepositoryCallback {
    fun scanStatus(isScanning: ScanningStatus) {}
    fun deviceListReady(deviceList: MutableList<String>) {}

    /**
     * Callback for connection status update.
     * @param device The bluetooth device
     * @param connected The connection status:
     *   * `null`: Connecting to the remote device
     *   * `true`: Connected successfully
     *   * `false`: Either disconnected or connection failed
     */
    fun onDeviceConnected(device: BluetoothDevice?, connected: Boolean?) {}
    fun onBtStateUpdate(enabled: Boolean) {}
}
