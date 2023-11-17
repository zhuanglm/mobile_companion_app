package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.home.repositories.HomeRepositoryListener
import com.esightcorp.mobile.app.home.state.HomeUiState
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.navigate
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    homeRepository: HomeRepository,
) : AndroidViewModel(application) {

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
        homeRepository.registerListener(listener)
        updateConnectedDevice(homeRepository.getConnectedDevice())
    }

    private fun updateConnectedDevice(device: String) {
        Log.d(TAG, "updateConnectedDevice: ${device}")
        _uiState.update { currentState ->
            currentState.copy(connectedDevice = device, isBluetoothConnected = true)
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
        navController.navigate("searching_for_networks/bluetooth")
    }

    fun navigateToBluetoothStart(navController: NavController) {
        navController.navigate("btconnection")
    }

    fun navigateToBluetoothDisabled(navController: NavController) {
        navController.navigate("bt_disabled")
    }

    fun navigateToWifiCredsQr(navController: NavController) {
        navController.navigate("searching_for_networks/qr")
    }

    fun navigateToShareYourView(navController: NavController) {
        navController.navigate("eshare")
    }

    fun navigateToSettings(navController: NavController) = navController.navigate(IncomingRoute)

    fun showFeedbackPage() = with(application.applicationContext) {
        openExternalUrl(getString(R.string.url_esight_feedback))
    }
}
