package com.esightcorp.mobile.app.home.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "HomeRepository"
class HomeRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val bluetoothModel: BluetoothModel
    private lateinit var repoListener: HomeRepositoryListener
    private val modelListener = object : BluetoothModelListener {
        override fun listOfDevicesUpdated() {
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
        }

        override fun onScanFailed(error: Int) {
        }

        override fun onScanStarted() {
        }

        override fun onScanFinished() {
        }

        override fun onScanCancelled() {
            TODO("Not yet implemented")
        }

        override fun onDeviceDisconnected(device: BluetoothDevice) {
            repoListener.onBluetoothDeviceDisconnected()
        }

        override fun onDeviceConnected(device: BluetoothDevice) {
            Log.i(TAG, "onDeviceConnected: $device")
        }

        override fun onConnectionStateQueried(state: Boolean) {
            Log.i(TAG, "onConnectionStateQueried: $state")
        }

        override fun onBluetoothEnabled() {
            repoListener.onBluetoothEnabled()
        }

        override fun onBluetoothDisabled() {
            repoListener.onBluetoothDisabled()
        }

        override fun onBluetoothStateQueried(state: Boolean) {
            Log.i(TAG, "onBluetoothStateQueried: $state")
        }
    }

    init {
        bluetoothModel = BluetoothModel(context = context)
        eSightBleManager.setModelListener(modelListener)
    }

    fun registerListener(listener: HomeRepositoryListener) {
        this.repoListener = listener
    }

    @SuppressLint("MissingPermission")
    fun getConnectedDevice(): String {
        return eSightBleManager.getConnectedDevice()!!.name
    }
}