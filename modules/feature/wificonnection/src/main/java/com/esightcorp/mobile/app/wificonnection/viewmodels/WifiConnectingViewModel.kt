package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepoListener
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
    val repository: WifiConnectionRepository
): AndroidViewModel(application){

    private var _uiState = MutableStateFlow(WifiConnectingUiState())
    val uiState: StateFlow<WifiConnectingUiState> = _uiState.asStateFlow()
    val repoListener = object: WifiConnectionRepoListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {

        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {

        }

        override fun onScanStatusUpdated(status: ScanningStatus) {

        }

        override fun onWifiConnected(success: Boolean) {
            updateConnectionStatus(success)
        }
    }

    init {
        repository.registerListener(repoListener)
        updateSsid(repository.getSelectedNetwork().SSID)
    }

    private fun updateSsid(ssid: String){
        _uiState.update { state ->
            state.copy(ssid = ssid)
        }
    }

    private fun updateConnectionStatus(status: Boolean){
        _uiState.update { state ->
            state.copy(connectionWasSuccess = status)
        }
    }





}