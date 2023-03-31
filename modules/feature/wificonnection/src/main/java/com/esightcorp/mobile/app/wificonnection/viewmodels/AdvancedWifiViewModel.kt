package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionListener
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiAdvancedSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AdvancedWifiViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(WifiAdvancedSettingsUiState())
    val uiState: StateFlow<WifiAdvancedSettingsUiState> = _uiState.asStateFlow()
    val TAG = "AdvancedWifiViewModel"
    private val listener = object : WifiConnectionListener {
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.d(TAG, "onBluetoothStatusUpdate: ")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            Log.d(TAG, "onWifiStatusUpdate: ")
        }

        override fun onWifiConnected(success: Boolean) {
            Log.d(TAG, "onWifiConnected: ")
        }

        override fun onWifiNetworkNotFound() {
            Log.d(TAG, "onWifiNetworkNotFound: ")
        }

        override fun onWifiConnectionTimeout() {
            Log.d(TAG, "onWifiConnectionTimeout: ")
        }

        override fun onWifiInvalidPassword() {
            Log.d(TAG, "onWifiInvalidPassword: ")
        }

        override fun onWifiWPALessThan8() {
            Log.d(TAG, "onWifiWPALessThan8: ")
        }

        override fun onWifiConnectionTest() {
            Log.d(TAG, "onWifiConnectionTest: ")
        }

        override fun onPlatformError() {
            Log.d(TAG, "onPlatformError: ")
        }

        override fun onGoWifiDisabled() {
            Log.d(TAG, "onGoWifiDisabled: ")
        }

        override fun onNetworkConnectionError() {
            Log.d(TAG, "onNetworkConnectionError: ")
        }


    }


    init {
        repository.registerListener(listener)
        refreshUiState()
    }

    fun refreshUiState() {
        onSsidUpdated(repository.getSelectedNetwork().SSID)
        onPasswordUpdated(repository.getCurrentPassword())
        onTypeUpdated(repository.getCurrentSecurityType())
    }

    override fun onCleared() {
        super.onCleared()
        repository.unregisterListener(listener)
    }

    fun onSsidUpdated(ssid: String) {
        _uiState.update { state ->
            state.copy(ssid = ssid)
        }
    }

    fun onPasswordUpdated(password: String) {
        _uiState.update { state ->
            state.copy(password = password)
        }
    }

    fun onTypeUpdated(type: String) {
        _uiState.update { state ->
            state.copy(wifiType = type)
        }
    }

    fun onBackButtonPressed(navController: NavController) {
        navController.popBackStack()
    }

    fun onSecurityButtonPressed(navController: NavController) {
        navController.navigate(WifiConnectionScreens.SelectNetworkSecurityRoute.route)
    }

    fun onFinishButtonPressed(navController: NavController) {
        Log.i(TAG, "onFinishButtonPressed: ")
    }


}