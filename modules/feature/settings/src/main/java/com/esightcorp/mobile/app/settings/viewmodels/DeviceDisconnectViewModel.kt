package com.esightcorp.mobile.app.settings.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.settings.state.DisconnectUiState
import com.esightcorp.mobile.app.settings.state.DisconnectUiState.State
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDisconnectViewModel @Inject constructor(
    application: Application,
    private val btConnRepo: BtConnectionRepository,
) : AndroidViewModel(application) {

    private val _disconnectUiState = MutableStateFlow(DisconnectUiState())
    val uiState: StateFlow<DisconnectUiState> = _disconnectUiState.asStateFlow()

    init {
        with(btConnRepo) {
            registerListener(
                object : BluetoothConnectionRepositoryCallback {
                    override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
                        _disconnectUiState.update {
                            it.copy(
                                disconnectState = if (connected) State.Connected
                                else State.Disconnected
                            )
                        }
                    }
                },
            )
            setupBtModelListener()
        }
    }

    fun disconnectToESight() {
        _disconnectUiState.update { it.copy(disconnectState = State.Disconnecting) }

        viewModelScope.launch(Dispatchers.IO) {
            delay(DISCONNECTING_DELAY)

            if (!btConnRepo.disconnectToDevice())
                _disconnectUiState.update { it.copy(disconnectState = null) }
        }
    }

    fun navigateBack(navController: NavController) = navController.popBackStack()

    fun navigateToDisconnectedScreen(navController: NavController) = navController.navigate(
        target = BtConnectionNavigation.IncomingRoute,
        popUntil = SettingsNavigation.IncomingRoute
    )

    companion object {
        /**
         * A delay time to show the disconnecting screen
         */
        const val DISCONNECTING_DELAY = 3000L // 3s
    }
}
