package com.esightcorp.mobile.app.home.state


data class HomeUiState(
    val isBluetoothConnected: Boolean = true,
    val isBluetoothEnabled: Boolean = true,
    val connectedDevice: String = "Devicename"
)
