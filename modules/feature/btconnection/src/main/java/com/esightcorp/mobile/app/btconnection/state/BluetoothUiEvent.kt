package com.esightcorp.mobile.app.btconnection.state

import android.bluetooth.BluetoothDevice

sealed class BluetoothUiEvent {

    data class BluetoothEnabled(val isEnabled: Boolean): BluetoothUiEvent()
    data class BluetoothConnected(val isConnected: Boolean, val currentDevice: BluetoothDevice): BluetoothUiEvent()
}

sealed class BtSearchingUiEvent: BluetoothUiEvent() {
    data class BluetoothDeviceFound(val bluetoothDeviceList: List<BluetoothDevice>) : BluetoothUiEvent()
}

sealed class BtDevicesUiEvent: BluetoothUiEvent(){
    data class BluetoothScanResponse(val bluetoothDeviceList: List<BluetoothDevice>) : BluetoothUiEvent()

}