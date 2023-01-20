package com.esightcorp.mobile.app.btconnection.state

import com.esightcorp.mobile.app.utils.ScanningStatus

data class BluetoothUiState(
    val btConnectionStatus: Boolean = false,
    val isBtEnabled: Boolean = false,
    val getConnectedDevice:String = "",
    val listOfAvailableDevices: List<String> = mutableListOf(),
    val deviceMapCache: List<String> = mutableListOf(),
    val isScanning: ScanningStatus = ScanningStatus.Unknown

)