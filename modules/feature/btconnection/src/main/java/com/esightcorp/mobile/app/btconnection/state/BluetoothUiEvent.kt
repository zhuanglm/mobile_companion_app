package com.esightcorp.mobile.app.btconnection.state

import android.bluetooth.BluetoothDevice

sealed class BluetoothUiEvent {

    data class BluetoothEnabled(val isEnabled: Boolean): BluetoothUiEvent()
    data class BluetoothConnected(val isConnected: Boolean, val currentDevice: BluetoothDevice): BluetoothUiEvent()
    data class BluetoothScanResponse(val bluetoothDeviceList: List<BluetoothDevice>) : BluetoothUiEvent()
}