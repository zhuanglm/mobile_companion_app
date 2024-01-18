package com.esightcorp.mobile.app.home.repositories

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import com.esightcorp.mobile.app.bluetooth.BluetoothModelListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val bluetoothModel: BluetoothModel
    private lateinit var repoListener: HomeRepositoryListener

    init {
        bluetoothModel = BluetoothModel(context).apply {
            bleManager.setModelListener(
                object : BluetoothModelListener {
                    override fun onDeviceDisconnected(device: BluetoothDevice?) {
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
