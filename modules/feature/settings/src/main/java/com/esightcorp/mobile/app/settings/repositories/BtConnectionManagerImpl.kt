package com.esightcorp.mobile.app.settings.repositories

import android.bluetooth.BluetoothDevice
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.settings.state.SettingConnectionState

class BtConnectionManagerImpl(private val btConnRepo: BtConnectionRepository) :
    IBtConnectionManager {

    override var connectionState = SettingConnectionState()

    override fun configureConnectionListener(onStateChanged: (SettingConnectionState) -> Unit) {
        // BtConnectionRepo
        with(btConnRepo) {
            registerListener(
                object : BluetoothConnectionRepositoryCallback {
                    override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean?) {
                        connectionState = connectionState.updateConnectionState(connected)
                        onStateChanged(connectionState)
                    }

                    override fun onBtStateUpdate(enabled: Boolean) {}
                },
            )
            setupBtModelListener()
        }
    }
}
