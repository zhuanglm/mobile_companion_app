package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
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

        override fun onWifiAlreadyConnected(status: Boolean) {
            Log.d(_tag, "onWifiAlreadyConnected: $status")
            updateWifiStatusState(status)
        }
    }

    init {
        repository.registerListener(repoListener)
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

    private fun updateWifiStatusState(status: Boolean) {
        _uiState.update { state ->
            state.copy(isWifiConnected = status)
        }
    }

    fun navigateToWifiNetworksScreen(navController: NavController) {
        navController.navigate(WifiNavigation.SelectNetworkRoute.path) {
            popUpTo(WifiNavigation.IncomingRoute.path) {
                inclusive = true
            }
        }
    }

    fun navigateToWifiAlreadyConnected(navController: NavController) {
        navController.navigate(WifiNavigation.AlreadyConnectedRoute.path) {
            popUpTo(WifiNavigation.IncomingRoute.path) {
                inclusive = true
            }
        }
    }

    fun setWifiFlow(flow: String?) {
        repository.setWifiFlow(flow!!)

        //start job according to flow type
        when (flow) {
            WifiNavigation.ScanningRoute.PARAM_WIFI_CONNECTION ->
                repository.readWifiConnectionStatus()

            //bluetooth and QR
            else ->
                repository.startWifiScan()
        }
    }
}
