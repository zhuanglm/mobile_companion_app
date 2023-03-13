package com.esightcorp.mobile.app.wificonnection.state

import android.bluetooth.BluetoothDevice

sealed class WifiConnectionUiEvent{
    data class PermissionsGranted(val areGranted: Boolean): WifiConnectionUiEvent()
    data class WifiEnabled(val isEnabled: Boolean): WifiConnectionUiEvent()

}
