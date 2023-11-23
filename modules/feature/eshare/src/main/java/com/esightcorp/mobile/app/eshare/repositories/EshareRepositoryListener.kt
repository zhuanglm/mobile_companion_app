package com.esightcorp.mobile.app.eshare.repositories

import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import java.io.InputStream

interface EshareRepositoryListener{
    fun onEshareStateChanged(state: EShareConnectionStatus)
    fun onEshareStateRequested(state : EShareConnectionStatus)
    fun onBluetoothDeviceDisconnected()
    fun onBluetoothDisabled()
    fun onWifiStateChanged(state: Boolean)
    fun onInputStreamCreated(inputStream: InputStream)
}