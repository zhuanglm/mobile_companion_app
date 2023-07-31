package com.esightcorp.mobile.app.eshare.repositories

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EshareRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val TAG = "EshareRepository"
    private val bluetoothModel: BluetoothModel
    private lateinit var eShareRepositoryListener: EshareRepositoryListener
    private val bluetoothModelListener = object : BluetoothModelListener{
        override fun listOfDevicesUpdated() {
            TODO("Not yet implemented")
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            TODO("Not yet implemented")
        }

        override fun onScanFailed(error: Int) {
            TODO("Not yet implemented")
        }

        override fun onScanStarted() {
            TODO("Not yet implemented")
        }

        override fun onScanFinished() {
            TODO("Not yet implemented")
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onBluetoothStateChanged() {
            TODO("Not yet implemented")
        }
    }

    init {
        bluetoothModel = BluetoothModel(context)
    }
    fun setupEshareListener(eshareRepositoryListener: EshareRepositoryListener) {
        eShareRepositoryListener = eshareRepositoryListener
    }
    
    fun startEshareConnection(){
        Log.i(TAG, "startEshareConnection: Lets get this thing going!!! ")
    }

    fun cancelEshareConnection(){
        Log.i(TAG, "cancelEshareConnection: Nevermind ")
    }
}