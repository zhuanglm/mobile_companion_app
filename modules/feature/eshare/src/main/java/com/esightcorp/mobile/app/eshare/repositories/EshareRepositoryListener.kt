package com.esightcorp.mobile.app.eshare.repositories

import com.esightcorp.mobile.app.utils.eShareConnectionStatus

interface EshareRepositoryListener{
    fun onEshareStateChanged(state: eShareConnectionStatus)
    fun onEshareStateRequested(state : eShareConnectionStatus)
    fun onBluetoothStateChanged(state: Boolean)
    fun onWifiStateChanged(state: Boolean)
}