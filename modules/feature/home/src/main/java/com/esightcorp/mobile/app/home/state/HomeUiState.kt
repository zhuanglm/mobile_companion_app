package com.esightcorp.mobile.app.home.state

import android.bluetooth.BluetoothDevice

data class HomeUiState(
    val isBluetoothConnected: Boolean = true,
    val isBluetoothEnabled: Boolean = false,
    val connectedDevice: String = "Devicename"
)
