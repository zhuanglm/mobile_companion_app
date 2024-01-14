package com.esightcorp.mobile.app.settings.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.settings.repositories.BtConnectionManagerImpl
import com.esightcorp.mobile.app.settings.repositories.IBtConnectionManager
import com.esightcorp.mobile.app.settings.state.SettingConnectionState
import com.esightcorp.mobile.app.ui.UI_DELAY_TIMER
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import com.esightcorp.mobile.app.utils.bluetooth.BleConnectionStatus
import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager
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
) : AndroidViewModel(application),
    IBtConnectionManager by BtConnectionManagerImpl(btConnRepo),
    IBleStateManager by BleStateManagerImpl() {

    private val _connState = MutableStateFlow(connectionState)
    val uiState: StateFlow<SettingConnectionState> = _connState.asStateFlow()

    init {
        configureConnectionListener { newState -> _connState.update { newState } }
    }

    fun disconnectToESight() {
        connectionState = connectionState.copy(state = BleConnectionStatus.Disconnecting)
        _connState.update { connectionState }

        viewModelScope.launch(Dispatchers.IO) {
            // Delay the UI changing so that user has time to read
            delay(UI_DELAY_TIMER)

            if (!btConnRepo.disconnectToDevice())
                _connState.update { it.copy(state = BleConnectionStatus.Unknown) }
        }
    }

    fun navigateBack(navController: NavController) = navController.popBackStack()

    fun navigateToDisconnectedScreen(navController: NavController) = navController.navigate(
        target = BtConnectionNavigation.IncomingRoute,
        popUntil = SettingsNavigation.IncomingRoute
    )
}
