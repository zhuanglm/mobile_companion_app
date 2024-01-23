package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.home.repositories.HomeRepositoryListener
import com.esightcorp.mobile.app.home.state.HomeUiState
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    homeRepository: HomeRepository,
) : AndroidViewModel(application),
    IBleStateManager by BleStateManagerImpl() {
    private val _tag = this.javaClass.simpleName

    /**
     * Object which is used by the compose UI to track UI State
     */
    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val listener = object : HomeRepositoryListener {
        override fun onBluetoothDisabled() {
            updateBtEnabledState(false)
            updateConnectedDevice("")
            updateConnectedState(false)
        }

        override fun onBluetoothEnabled() {
            updateBtEnabledState(true)
        }

        override fun onBluetoothDeviceDisconnected() {
            updateConnectedDevice("")
            updateConnectedState(false)
        }
    }

    init {
        with(homeRepository) {
            registerListener(listener)

            when (val connectedDev = getConnectedDevice()) {
                null -> updateConnectedState(false)
                else -> updateConnectedDevice(connectedDev)
            }
        }
        updateConnectedDevice(homeRepository.getConnectedDevice())
    }

    private fun updateConnectedDevice(device: String?) {
        Log.d(_tag, "updateConnectedDevice: $device")
        device?.let {
            _uiState.update { currentState ->
                currentState.copy(connectedDevice = it, isBluetoothConnected = true)
            }
        }
    }

    private fun updateConnectedState(status: Boolean) {
        _uiState.update { state ->
            state.copy(isBluetoothConnected = status)
        }
    }

    private fun updateBtEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isBluetoothEnabled = enabled)
        }
    }

    fun navigateToWifiCredsOverBt(navController: NavController) {
        navController.navigate(
            target = WifiNavigation.ScanningRoute,
            param = WifiNavigation.ScanningRoute.PARAM_WIFI_CONNECTION
        )
    }

    fun navigateToBluetoothStart(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.IncomingRoute)
    }

    fun navigateToBluetoothDisabled(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.BtDisabledScreen)
    }

    fun navigateToShareYourView(navController: NavController) {
        navController.navigate(EShareNavigation.IncomingRoute)
    }

    fun navigateToSettings(navController: NavController) = navController.navigate(IncomingRoute)

    fun showFeedbackPage() = with(application.applicationContext) {
        openExternalUrl(getString(R.string.url_esight_feedback))
    }
}
