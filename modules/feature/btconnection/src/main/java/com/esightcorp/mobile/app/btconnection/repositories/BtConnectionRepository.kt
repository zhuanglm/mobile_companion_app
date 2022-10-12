package com.esightcorp.mobile.app.btconnection.repositories

import android.content.Context
import com.esightcorp.mobile.app.bluetooth.BluetoothModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BtConnectionRepository @Inject constructor(
   @ApplicationContext context: Context
): IBtConnectionRepository {

    private val bluetoothModel = BluetoothModel(context)

    fun getListOfDevices(){
        bluetoothModel.logPairedDevices()
    }

    fun checkBtConnectionState(){
        bluetoothModel.isBluetoothCurrentlyConnected()
    }




}