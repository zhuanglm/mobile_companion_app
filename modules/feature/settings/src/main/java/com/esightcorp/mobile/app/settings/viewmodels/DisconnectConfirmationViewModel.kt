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
import com.esightcorp.mobile.app.ui.UI_DELAY_TIMER
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
class DisconnectConfirmationViewModel @Inject constructor(
    application: Application,
    private val btConnRepo: BtConnectionRepository,
) : AndroidViewModel(application) {

    private val _disconnectUiState = MutableStateFlow(DisconnectUiState())
    val uiState: StateFlow<DisconnectUiState> = _disconnectUiState.asStateFlow()

    init {
        with(btConnRepo) {
            registerListener(
                object : BluetoothConnectionRepositoryCallback {
                    override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean?) {
                        _disconnectUiState.update {
                            it.copy(
                                disconnectState = if (connected == true) State.Connected
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
            // Delay the UI changing so that user has time to read
            delay(UI_DELAY_TIMER)

            if (!btConnRepo.disconnectToDevice())
                _disconnectUiState.update { it.copy(disconnectState = null) }
        }
    }

    fun navigateBack(navController: NavController) = navController.popBackStack()

    fun navigateToDisconnectedScreen(navController: NavController) = navController.navigate(
        target = BtConnectionNavigation.IncomingRoute,
        popUntil = SettingsNavigation.IncomingRoute
    )
}
