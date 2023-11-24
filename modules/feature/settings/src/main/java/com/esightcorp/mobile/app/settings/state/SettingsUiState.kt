package com.esightcorp.mobile.app.settings.state

data class SettingsUiState(
    val appVersion: String? = null,
    val isConnected: Boolean? = null,
)

data class DisconnectUiState(
    val disconnectState: State? = null
) {
    enum class State {
        Connected,
        Disconnecting,
        Disconnected
    }
}
