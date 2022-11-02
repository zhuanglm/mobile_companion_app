package com.esightcorp.mobile.app.btconnection.state

import com.esightcorp.mobile.app.utils.ScanningStatus

data class BluetoothUiState(
    val btConnectionStatus: Boolean = false,
    val isBtEnabled: Boolean = true,
    val arePermissionsGranted: Boolean = false,
    val getConnectedDevice:String = "",
    val listOfAvailableDevices: List<String> = mutableListOf(),
    val deviceMapCache: List<String> = mutableListOf(),
    val isScanning: com.esightcorp.mobile.app.utils.ScanningStatus = com.esightcorp.mobile.app.utils.ScanningStatus.Unknown

) {
}