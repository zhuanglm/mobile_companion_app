package com.esightcorp.mobile.app.home.state

import android.bluetooth.BluetoothDevice

sealed class HomeUiEvent{
    data class PermissionsGranted(val areGranted: Boolean): HomeUiEvent()
    data class BluetoothEnabled(val isEnabled: Boolean): HomeUiEvent()
    data class BluetoothConnected(val isConnected: Boolean, val currentDevice: BluetoothDevice): HomeUiEvent()
}
