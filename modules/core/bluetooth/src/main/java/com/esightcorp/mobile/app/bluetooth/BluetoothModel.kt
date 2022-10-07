package com.esightcorp.mobile.app.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService

class BluetoothModel constructor(
    context: Context
) {
    val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

    init {
        //check bluetooth permissions
        if(bluetoothAdapter?.isEnabled == false){
        }


    }
}