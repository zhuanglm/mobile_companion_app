package com.esightcorp.mobile.app.home.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import com.esightcorp.mobile.app.bluetooth.IBleEventListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    @ApplicationContext context: Context,
    bleEventListener: IBleEventListener,
) {
    private val _tag = this.javaClass.simpleName

    private val bluetoothModel: BluetoothModel
    private lateinit var repoListener: HomeRepositoryListener

    init {
        Log.w(_tag, "bleEventListener - $bleEventListener")
        bluetoothModel = BluetoothModel(context).apply {
            initialize(
                object : BluetoothModelListener {
                    override fun onDeviceDisconnected(device: BluetoothDevice) {
                        repoListener.onBluetoothDeviceDisconnected()
                    }

                    override fun onBluetoothEnabled() {
                        repoListener.onBluetoothEnabled()
                    }

                    override fun onBluetoothDisabled() {
                        repoListener.onBluetoothDisabled()
                        bleManager.resetConnectedDevice()
                    }
                },
                bleEventListener,
            )
        }
    }

    fun registerListener(listener: HomeRepositoryListener) {
        this.repoListener = listener
    }

    @SuppressLint("MissingPermission")
    fun getConnectedDevice(): String? {
        return bluetoothModel.bleManager.getConnectedDevice()?.name
    }
}
