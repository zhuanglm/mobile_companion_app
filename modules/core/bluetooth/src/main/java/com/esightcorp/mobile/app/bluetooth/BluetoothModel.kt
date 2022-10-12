package com.esightcorp.mobile.app.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.math.log

class BluetoothModel constructor(
    val context: Context
):IBluetoothModel {
    val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    @SuppressLint("MissingPermission")
    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

    @SuppressLint("MissingPermission")
    fun logPairedDevices(){
        pairedDevices?.forEach{device ->
            Log.d("TAG", "logPairedDevices: Device name: ${device.name} ")
            Log.d("TAG", "logPairedDevices: hardware address ${device.address}")

        }
    }

    @SuppressLint("MissingPermission")
    fun isBluetoothCurrentlyConnected(){
        val a2dpState = bluetoothAdapter?.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothAdapter.STATE_CONNECTED
        val gattState = bluetoothAdapter?.getProfileConnectionState(BluetoothProfile.GATT) == BluetoothAdapter.STATE_CONNECTED
        val hidState = bluetoothAdapter?.getProfileConnectionState(BluetoothProfile.HID_DEVICE) == BluetoothAdapter.STATE_CONNECTED
        Log.d("TAG", "isBluetoothCurrentlyConnected: a2dp ${a2dpState}, gatt ${gattState}, hid ${hidState} ")

    }




}