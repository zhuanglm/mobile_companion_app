package com.esightcorp.mobile.app.home.state

import android.bluetooth.BluetoothDevice

data class HomeUiState(
    val isBluetoothConnected: Boolean = false,
    val isBluetoothEnabled: Boolean = false,
    val arePermissionsGranted: Boolean = false,
)
