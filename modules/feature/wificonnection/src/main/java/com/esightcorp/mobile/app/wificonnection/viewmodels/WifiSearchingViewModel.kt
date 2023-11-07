package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiSearchingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WifiSearchingViewModel @Inject constructor(
    application: Application,
    private val repository: WifiConnectionRepository
) : AndroidViewModel(application) {
    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiSearchingUiState())
    val uiState: StateFlow<WifiSearchingUiState> = _uiState.asStateFlow()

    private val repoListener = object : WifiNetworkScanListener {
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.i(_tag, "onBluetoothStatusUpdate: ")
            updateBtEnabledState(status)
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.d(_tag, "onNetworkListUpdated: ")
            updateScanningStatus(ScanningStatus.Success)
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.d(_tag, "onScanStatusUpdated: $status")
            updateScanningStatus(status)
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            Log.d(_tag, "onWifiStatusUpdate: $status")
            updateWifiEnabledState(status)
        }
    }

    init {
        repository.registerListener(repoListener)
        repository.startWifiScan()
    }

    private fun updateScanningStatus(scanningStatus: ScanningStatus) {
        _uiState.update { state ->
            state.copy(scanningStatus = scanningStatus)
        }
    }

    private fun updateWifiEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isWifiEnabled = enabled)
        }
    }

    private fun updateBtEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isBtEnabled = enabled)
        }
    }

    fun navigateToWifiNetworksScreen(navController: NavController) {
        navController.navigate(WifiConnectionScreens.SelectNetworkRoute.route) {
            popUpTo(WifiConnectionScreens.IncomingNavigationRoute.route) {
                inclusive = true
            }
        }
    }

    fun setWifiFlow(flow: String?) {
        repository.setWifiFlow(flow!!)
    }
}
