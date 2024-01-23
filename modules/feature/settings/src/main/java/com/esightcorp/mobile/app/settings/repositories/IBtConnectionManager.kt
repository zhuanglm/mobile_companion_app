package com.esightcorp.mobile.app.settings.repositories

import com.esightcorp.mobile.app.settings.state.SettingConnectionState

interface IBtConnectionManager {
    fun configureConnectionListener(onStateChanged: (SettingConnectionState) -> Unit)
    var connectionState: SettingConnectionState
}
