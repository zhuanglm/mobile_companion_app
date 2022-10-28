package com.esightcorp.mobile.app.btconnection.state

import android.bluetooth.BluetoothDevice
import com.esightcorp.mobile.app.btconnection.repositories.ScanningStatus

data class BluetoothUiState(
    val btConnectionStatus: Boolean = false,
    val isBtEnabled: Boolean = true,
    val arePermissionsGranted: Boolean = false,
    val getConnectedDevice:String = "",
    val listOfAvailableDevices: List<String> = mutableListOf(),
    val deviceMapCache: HashMap<String, Boolean> = hashMapOf(),
    val isScanning: ScanningStatus = ScanningStatus.Unknown

) {
}