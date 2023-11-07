package com.esightcorp.mobile.app.settings.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.settings.repositories.SettingsRepository
import com.esightcorp.mobile.app.settings.state.SettingsUiState
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    settingsRepo: SettingsRepository,
    btConnRepo: BtConnectionRepository,
) : AndroidViewModel(application) {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    init {
        _settingsUiState.update { it.copy(appVersion = settingsRepo.getApplicationVersion()) }

        // BtConnectionRepo
        with(btConnRepo) {
            registerListener(
                object : BluetoothConnectionRepositoryCallback {
                    override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
                        _settingsUiState.update { it.copy(isConnected = connected) }
                    }

                    override fun scanStatus(isScanning: ScanningStatus) {}

                    override fun deviceListReady(deviceList: MutableList<String>) {}

                    override fun onBtStateUpdate(enabled: Boolean) {}
                },
            )
            setupBtModelListener()
        }
    }
}
