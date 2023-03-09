package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepoListener
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
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
    repository: WifiConnectionRepository
): AndroidViewModel(application) {
    private val TAG = "WifiSearchingViewModel"
    private var _uiState = MutableStateFlow(WifiSearchingUiState())
    val uiState: StateFlow<WifiSearchingUiState> = _uiState.asStateFlow()

    val repoListener = object : WifiConnectionRepoListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            updateScanningStatus(ScanningStatus.Success)
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.d(TAG, "onScanStatusUpdated: $status")
            updateScanningStatus(status)
        }

        override fun onWifiConnected(success: Boolean) {
            Log.i(TAG, "onWifiConnected: " + success)
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            TODO("Not yet implemented")
        }
    }

    init {
        repository.registerListener(repoListener)
        repository.startWifiScan()
    }

    private fun updateScanningStatus(scanningStatus: ScanningStatus){
        _uiState.update { state ->
            state.copy(scanningStatus = scanningStatus)
        }
    }


    fun navigateToWifiNetworksScreen(navController: NavController){
        navController.navigate(WifiConnectionScreens.SelectNetworkRoute.route)
    }

}