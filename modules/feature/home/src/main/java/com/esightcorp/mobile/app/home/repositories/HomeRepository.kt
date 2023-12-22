package com.esightcorp.mobile.app.home.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
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
        override fun onDeviceDisconnected(device: BluetoothDevice) {
            repoListener.onBluetoothDeviceDisconnected()
        }

        override fun onBluetoothEnabled() {
            repoListener.onBluetoothEnabled()
        }

        override fun onBluetoothDisabled() {
            repoListener.onBluetoothDisabled()
            eSightBleManager.resetConnectedDevice()
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
    fun getConnectedDevice(): String? {
        return eSightBleManager.getConnectedDevice()?.name
    }
}