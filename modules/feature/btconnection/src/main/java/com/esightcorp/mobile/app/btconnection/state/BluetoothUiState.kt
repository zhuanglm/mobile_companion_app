package com.esightcorp.mobile.app.btconnection.state

import android.bluetooth.BluetoothDevice

data class BluetoothUiState(
    val isBluetoothConnected: Boolean = false,
    val isBluetoothEnabled: Boolean = false,
    val arePermissionsGranted: Boolean = false,
    val getConnectedDevice: BluetoothDevice? = null,
    val listOfAvailableDevices: List<BluetoothDevice>? = null
) {
}