/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.WifiType
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.ui.WPA_MAX_PASSWORD_LENGTH
import com.esightcorp.mobile.app.ui.WPA_MIN_PASSWORD_LENGTH
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EnterPasswordViewModel @Inject constructor(
    application: Application,
    private val repository: WifiConnectionRepository,
) : AndroidViewModel(application),
    WifiBleConnectionStateManager by WifiBleConnectionStateManagerImpl(repository) {

    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiCredentialsUiState())
    val uiState: StateFlow<WifiCredentialsUiState> = _uiState.asStateFlow()

    private val scanListener = object : WifiNetworkScanListener {
        override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
            updateBleConnectionState(isConnected)
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.i(_tag, "onNetworkListUpdated: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(_tag, "onScanStatusUpdated: ")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            Log.i(_tag, "onWifiStatusUpdate: ")
        }

        override fun onWifiAlreadyConnected(status: Boolean) {
            Log.i(_tag, "onWifiAlreadyConnected: $status")
        }
    }

    private val wifiType: WifiType
    private val ssid: String?

    init {
        repository.registerListener(scanListener)
        wifiType = repository.wifiCredentials.getWifiType()
        ssid = repository.wifiCredentials.getSSID()
        updateWifiFlow(repository.wifiFlow)
    }

    private fun isPasswordValid(password: String): Boolean {
        return when (wifiType) {
            WifiType.WPA -> password.length in WPA_MIN_PASSWORD_LENGTH..WPA_MAX_PASSWORD_LENGTH
            WifiType.WEP -> when (password.length) {
                10, 26, 58 -> true
                else -> false
            }

            WifiType.NONE -> true
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { state ->
            state.copy(
                password = password,
                isPasswordValid = isPasswordValid(password)
            )
        }
    }

    private fun updateWifiFlow(flow: WifiCache.WifiFlow) {
        _uiState.update { state ->
            state.copy(wifiFlow = flow)
        }
    }

    fun wifiPasswordSubmitted(navController: NavController) {
        _uiState.update { state ->
            state.copy(passwordSubmitted = true)
        }
        //make sure selected ssid and wifi type not be changed
        repository.setWifiNetwork(ssid ?: "", wifiType, _uiState.value.password)
        if (_uiState.value.wifiFlow == WifiCache.WifiFlow.BluetoothFlow) {
            sendWifiCredsViaBluetooth()
            navigateToConnectingScreen(navController)
        } else if (_uiState.value.wifiFlow == WifiCache.WifiFlow.QrFlow) {
            navigateToQrScreen(navController)
        }
    }

    fun onAdvancedButtonPressed(navController: NavController) {
        repository.setWifiPassword(_uiState.value.password)

        navController.navigate(
            target = WifiNavigation.AdvancedNetworkSettingsRoute,
            popCurrent = false
        )
    }

    fun onBackButtonPressed(navController: NavController) {
        navController.popBackStack()
    }

    private fun sendWifiCredsViaBluetooth() {
        Log.d(_tag, "sendWifiCredsViaBluetooth: ")
        repository.sendWifiCreds(_uiState.value.password, wifiType.typeString)
    }

    private fun navigateToConnectingScreen(navController: NavController) {
        Log.i(_tag, "navigateToConnectingScreen: Bluetooth route selected")
        navController.navigate(WifiNavigation.ConnectingRoute, popCurrent = false)
    }

    private fun navigateToQrScreen(navController: NavController) {
        Log.i(_tag, "navigateToQrScreen: QR Route selected")
        navController.navigate(WifiNavigation.WifiQRCodeRoute, popCurrent = false)
    }
}
