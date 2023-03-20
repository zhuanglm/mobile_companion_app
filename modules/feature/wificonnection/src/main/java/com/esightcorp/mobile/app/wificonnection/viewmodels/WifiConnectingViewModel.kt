package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.utils.ScanningStatus
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
    val repository: WifiConnectionRepository
): AndroidViewModel(application){

    private var _uiState = MutableStateFlow(WifiConnectingUiState())
    val uiState: StateFlow<WifiConnectingUiState> = _uiState.asStateFlow()
    val TAG = "WifiConnectingViewModel"
    val repoListener = object: WifiConnectionListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.i(TAG, "onBluetoothStatusUpdate: ")
        }

        override fun onWifiConnected(success: Boolean) {
            updateConnectionStatus(success)
        }

        override fun onWifiNetworkNotFound() {
            Log.e(TAG, "onWifiNetworkNotFound: ", )
        }

        override fun onWifiConnectionTimeout() {
            Log.e(TAG, "onWifiConnectionTimeout: ", )
        }

        override fun onWifiInvalidPassword() {
            Log.e(TAG, "onWifiInvalidPassword: ", )
        }

        override fun onWifiWPALessThan8() {
            Log.e(TAG, "onWifiWPALessThan8: ", )
        }

        override fun onWifiConnectionTest() {
            Log.e(TAG, "onWifiConnectionTest: ", )
        }

        override fun onPlatformError() {
            Log.e(TAG, "onPlatformError: ", )
        }

        override fun onGoWifiDisabled() {
            Log.e(TAG, "onGoWifiDisabled: ", )
        }

        override fun onNetworkConnectionError() {
            Log.e(TAG, "onNetworkConnectionError: ", )
            onWifiConnectionError()
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            updateWifiEnabledState(status)
        }
    }

    init {
        repository.registerListener(repoListener)
    }

    fun getUpdatedSSIDFromRepo(){
        updateSsid(repository.getSelectedNetwork().SSID)
    }

    private fun updateSsid(ssid: String){
        _uiState.update { state ->
            state.copy(ssid = ssid)
        }
    }

    private fun onWifiConnectionError(){
        _uiState.update { state ->
            state.copy(connectionError = true)
        }
    }
    
    private fun updateWifiEnabledState(enabled: Boolean){
        _uiState.update { state -> 
            state.copy(isWifiEnabled = enabled)
        }
    }

    private fun updateConnectionStatus(status: Boolean){
        _uiState.update { state ->
            state.copy(connectionWasSuccess = status)
        }
    }





}