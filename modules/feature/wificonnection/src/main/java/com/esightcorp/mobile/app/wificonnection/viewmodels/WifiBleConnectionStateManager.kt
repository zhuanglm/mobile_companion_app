package com.esightcorp.mobile.app.wificonnection.viewmodels

import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager
import kotlinx.coroutines.flow.StateFlow

interface WifiBleConnectionStateManager : IBleStateManager {
    fun updateBleConnectionState(isConnected: Boolean)

    fun connectionStateFlow(): StateFlow<Boolean?>
}
