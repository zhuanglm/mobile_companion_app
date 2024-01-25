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
        refreshUiState()
    }

    fun refreshUiState() {
        with(repository.wifiCredentials) {
            when (val ssid = getSSID()) {
                 null -> Log.e(_tag, "SSID is null! Is there a valid wifi selected?")

                else -> _uiState.update {
                    it.copy(ssid = ssid, password = getPassword(), wifiType = getWifiType())
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.unregisterListener(listener)
    }

    fun onSsidUpdated(ssid: String) = _uiState.update { state ->
        state.copy(ssid = ssid)
    }

    fun onPasswordUpdated(password: String) = _uiState.update { state ->
        state.copy(password = password)
    }

    fun onBackButtonPressed(navController: NavController) {
        navController.popBackStack()
    }

    fun onSecurityButtonPressed(navController: NavController) {
        navController.navigate(WifiConnectionScreens.SelectNetworkSecurityRoute.route)
    }

    fun onFinishButtonPressed(navController: NavController) {
        Log.i(_tag, "onFinishButtonPressed: ")
    }
}
