/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

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
