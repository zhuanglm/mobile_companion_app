package com.esightcorp.mobile.app.home.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.eSightBleManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

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

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {

        }

        override fun onBluetoothStateChanged() {
            if (eSightBleManager.checkIfEnabled()) {
                repoListener.onBluetoothEnabled()
            } else {
                repoListener.onBluetoothDisabled()
            }
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