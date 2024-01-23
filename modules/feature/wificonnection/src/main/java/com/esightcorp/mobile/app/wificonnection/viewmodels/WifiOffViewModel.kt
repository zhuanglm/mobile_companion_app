package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiOffUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WifiOffViewModel @Inject constructor(
    application: Application,
    private val repository: WifiConnectionRepository,
) : AndroidViewModel(application),
    WifiBleConnectionStateManager by WifiBleConnectionStateManagerImpl(repository) {

    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiOffUiState())
    val uiState: StateFlow<WifiOffUiState> = _uiState.asStateFlow()

    private val listener = object : WifiNetworkScanListener {
        override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
            updateBleConnectionState(isConnected)
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.e(_tag, "onNetworkListUpdated: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.e(_tag, "onScanStatusUpdated: This should not be called")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            _uiState.value = _uiState.value.copy(isWifiEnabled = status)
        }

        override fun onWifiAlreadyConnected(status: Boolean) {
            Log.i(_tag, "onWifiAlreadyConnected: $status")
        }
    }

    init {
        repository.registerListener(listener)
    }

    fun onRetryPressed(navController: NavController) {
        if (!_uiState.value.isWifiEnabled) {
            Log.e(_tag, "onRetryPressed - ignored as wifi is still off!")
            return
        }

        val targetRoute = when (repository.wifiFlow) {
            WifiCache.WifiFlow.QrFlow -> WifiNavigation.ScanningRoute.PARAM_QR
            else -> WifiNavigation.ScanningRoute.PARAM_BLUETOOTH
        }

        navController.navigate(
            target = WifiNavigation.ScanningRoute,
            param = targetRoute
        )
    }

    fun onDismissed(navController: NavController) {
        navController.popBackStack()
    }
}
