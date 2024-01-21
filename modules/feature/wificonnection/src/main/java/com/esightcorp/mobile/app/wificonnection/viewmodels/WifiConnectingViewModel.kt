package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionListener
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class WifiConnectingViewModel @Inject constructor(
    application: Application,
    repository: WifiConnectionRepository
) : AndroidViewModel(application),
    WifiBleConnectionStateManager by WifiBleConnectionStateManagerImpl(repository) {

    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiConnectingUiState())
    val uiState: StateFlow<WifiConnectingUiState> = _uiState.asStateFlow()

    //region WifiConnectionListener
    private val repoListener = object : WifiConnectionListener {
        override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
            updateBleConnectionState(isConnected)
        }

        override fun onWifiConnected(success: Boolean) {
            updateConnectionStatus(success)
        }

        override fun onNetworkConnectionError() {
            Log.e(_tag, "onNetworkConnectionError: ")
            onWifiConnectionError()
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            updateWifiEnabledState(status)
        }
    }
    //endregion

    init {
        repository.registerListener(repoListener)
        when (val ssid = repository.wifiCredentials.getSSID()) {
            null -> Log.e(_tag, "SSID is null!!!")
            else -> updateSsid(ssid)
        }
    }

    //region Navigation
    fun navigateToWifiConnected(navController: NavController) = with(navController) {
        navigate(WifiNavigation.ConnectedRoute)
    }

    fun navigateToWifiConnectError(navController: NavController) = with(navController) {
        navigate(WifiNavigation.UnableToConnectRoute)
    }
    //endregion

    //region Private implementation
    private fun updateSsid(ssid: String) = _uiState.update { state ->
        state.copy(ssid = ssid)
    }

    private fun onWifiConnectionError() = _uiState.update { state ->
        state.copy(connectionError = true)
    }

    private fun updateWifiEnabledState(enabled: Boolean) = _uiState.update { state ->
        state.copy(isWifiEnabled = enabled)
    }

    private fun updateConnectionStatus(status: Boolean) = _uiState.update { state ->
        state.copy(connectionWasSuccess = status)
    }
    //endregion
}
