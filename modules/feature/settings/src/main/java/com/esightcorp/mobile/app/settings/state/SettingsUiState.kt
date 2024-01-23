package com.esightcorp.mobile.app.settings.state

import android.util.Log
import com.esightcorp.mobile.app.utils.bluetooth.BleConnectionStatus

data class SettingsUiState(
    val appVersion: String? = null,
    val connState: SettingConnectionState? = null,
)

data class SettingConnectionState(
    val state: BleConnectionStatus = BleConnectionStatus.Unknown,
    val isConnectionDropped: Boolean? = null
) {
    private val _tag = this.javaClass.simpleName

    fun updateConnectionState(connected: Boolean?): SettingConnectionState {
        Log.w(_tag, "updateConnectionState: $this -> connected: $connected")
        return when (connected) {
            true -> copy(state = BleConnectionStatus.Connected, isConnectionDropped = null)
            else -> when (state) {
                BleConnectionStatus.Connected -> copy(
                    state = BleConnectionStatus.Disconnected,
                    isConnectionDropped = true
                )

                else -> copy(state = BleConnectionStatus.Disconnected)
            }
        }.also {
            Log.w(_tag, "updateConnectionState ==> $it")
        }
    }

    val isConnected = (state == BleConnectionStatus.Connected)
}
