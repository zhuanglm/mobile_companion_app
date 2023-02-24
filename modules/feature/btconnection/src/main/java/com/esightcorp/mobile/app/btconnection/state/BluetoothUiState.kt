package com.esightcorp.mobile.app.btconnection.state

import com.esightcorp.mobile.app.utils.ScanningStatus

data class BluetoothUiState(
    val btConnectionStatus: Boolean = false,
    val isBtEnabled: Boolean = false,
    val connectedDevice:String = "",

)

data class BtSearchingUiState(
    val isScanning: ScanningStatus = ScanningStatus.Unknown,
    val isBtEnabled: Boolean = false,
)

data class BtDevicesUiState(
    val listOfAvailableDevices: List<String> = mutableListOf(),
    val isBtEnabled: Boolean = false,
    val deviceMapCache: List<String> = mutableListOf()
)

data class BtConnectingUiState(
    val didDeviceConnect: Boolean = false,
    val deviceName: String? = null,
    val deviceAddress: String? = null
    )


