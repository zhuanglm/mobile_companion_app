package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.util.Log
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WifiBleConnectionStateManagerImpl(private val wifiRepo: WifiConnectionRepository) :
    WifiBleConnectionStateManager,
    IBleStateManager by BleStateManagerImpl() {

    private val _tag = this.javaClass.simpleName

    private val _connState: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    override fun updateBleConnectionState(isConnected: Boolean) {
        Log.w(_tag, "updateBleConnectionState ->> wifiFlow: ${wifiRepo.wifiFlow}")

        when (wifiRepo.wifiFlow) {
            WifiCache.WifiFlow.BluetoothFlow -> _connState.update { isConnected }
            else -> {
                // ignore updating status for all other flow types
                Log.w(_tag, "Ignore bleState update for ${wifiRepo.wifiFlow}")
            }
        }
    }

    override fun connectionStateFlow(): StateFlow<Boolean?> = _connState.asStateFlow()
}
