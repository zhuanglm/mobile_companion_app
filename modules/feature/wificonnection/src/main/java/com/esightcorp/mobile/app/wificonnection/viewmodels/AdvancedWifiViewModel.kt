/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.WifiType
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.ui.WPA_MAX_PASSWORD_LENGTH
import com.esightcorp.mobile.app.ui.WPA_MIN_PASSWORD_LENGTH
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
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
    private val repository: WifiConnectionRepository
) : AndroidViewModel(application),
    WifiBleConnectionStateManager by WifiBleConnectionStateManagerImpl(repository) {

    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiAdvancedSettingsUiState())
    val uiState: StateFlow<WifiAdvancedSettingsUiState> = _uiState.asStateFlow()

    private val listener = object : WifiConnectionListener {
        override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
            updateBleConnectionState(isConnected)
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            Log.d(_tag, "onWifiStatusUpdate: ")
        }

        override fun onWifiConnected(success: Boolean) {
            Log.d(_tag, "onWifiConnected: ")
        }

        override fun onWifiNetworkNotFound() {
            Log.d(_tag, "onWifiNetworkNotFound: ")
        }

        override fun onWifiConnectionTimeout() {
            Log.d(_tag, "onWifiConnectionTimeout: ")
        }

        override fun onWifiInvalidPassword() {
            Log.d(_tag, "onWifiInvalidPassword: ")
        }

        override fun onWifiWPALessThan8() {
            Log.d(_tag, "onWifiWPALessThan8: ")
        }

        override fun onWifiConnectionTest() {
            Log.d(_tag, "onWifiConnectionTest: ")
        }

        override fun onPlatformError() {
            Log.d(_tag, "onPlatformError: ")
        }

        override fun onGoWifiDisabled() {
            Log.d(_tag, "onGoWifiDisabled: ")
        }

        override fun onNetworkConnectionError() {
            Log.d(_tag, "onNetworkConnectionError: ")
        }
    }

    init {
        repository.registerListener(listener)
        updateWifiFlow(repository.wifiFlow)
        refreshUiState()
    }

    private fun updateWifiFlow(flow: WifiCache.WifiFlow) {
        _uiState.update { state ->
            state.copy(wifiFlow = flow)
        }
    }

    fun refreshUiState() {
        with(repository.wifiCredentials) {
            when (val ssid = getSSID()) {
                null -> {
                    Log.e(_tag, "SSID is null! Is there a valid wifi selected?")
                    _uiState.update {
                        it.copy(wifiType = getWifiType(),
                            isPasswordValid = false)
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            ssid = ssid,
                            password = getPassword(),
                            wifiType = getWifiType(),
                            isPasswordValid = isPasswordValid(ssid, getPassword())
                        )
                    }
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.unregisterWifiConnectionListener()
    }

    private fun isPasswordValid(ssid: String, password: String) : Boolean {
        if(ssid == "") return false
        with(_uiState.value) {
            return when(wifiType) {
                WifiType.WPA -> password.length in WPA_MIN_PASSWORD_LENGTH..WPA_MAX_PASSWORD_LENGTH
                WifiType.WEP -> when(password.length) {
                    10,26,58 -> true
                    else -> false
                }
                WifiType.NONE -> true
            }
        }

    }

    fun onSsidUpdated(ssid: String) = _uiState.update { state ->
        state.copy(ssid = ssid, isPasswordValid = isPasswordValid(ssid, _uiState.value.password))
    }

    fun onPasswordUpdated(password: String) = _uiState.update { state ->
        state.copy(password = password, isPasswordValid = isPasswordValid(_uiState.value.ssid, password)
        )
    }

    fun onBackButtonPressed(navController: NavController) {
        navController.popBackStack()
    }

    fun onSecurityButtonPressed(navController: NavController) {
        //avoid to lost user input
        repository.setWifiNetwork(_uiState.value.ssid, _uiState.value.wifiType, _uiState.value.password)
        navController.navigate(WifiNavigation.SelectNetworkSecurityRoute, popCurrent = false)
    }

    private fun navigateToQrScreen(navController: NavController) {
        Log.i(_tag, "navigateToQrScreen: QR Route selected")
        navController.navigate(WifiNavigation.WifiQRCodeRoute, popCurrent = false)
    }

    private fun sendWifiCredsViaBluetooth() {
        Log.d("WifiCredentialsViewModel", "sendWifiCredsViaBluetooth: ")
        repository.sendWifiCreds(_uiState.value.password,_uiState.value.wifiType.typeString)
    }

    private fun navigateToConnectingScreen(navController: NavController) {
        Log.i(_tag, "navigateToConnectingScreen: Bluetooth route selected")
        navController.navigate(WifiNavigation.ConnectingRoute, popCurrent = false)
    }

    fun onFinishButtonPressed(navController: NavController) {
        Log.i(_tag, "onFinishButtonPressed: ")
        _uiState.update { state ->
            state.copy(passwordSubmitted = true)
        }
        repository.setWifiNetwork(_uiState.value.ssid, _uiState.value.wifiType, _uiState.value.password)

        if (_uiState.value.wifiFlow == WifiCache.WifiFlow.BluetoothFlow) {
            sendWifiCredsViaBluetooth()
            navigateToConnectingScreen(navController)
        } else if (_uiState.value.wifiFlow == WifiCache.WifiFlow.QrFlow) {
            navigateToQrScreen(navController)
        }

    }
}
