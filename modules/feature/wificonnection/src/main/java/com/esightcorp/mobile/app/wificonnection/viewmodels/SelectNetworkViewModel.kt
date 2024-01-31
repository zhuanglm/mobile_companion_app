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
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.SelectNetworkUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SelectNetworkViewModel @Inject constructor(
    application: Application,
    private val wifiRepository: WifiConnectionRepository
) : AndroidViewModel(application),
    WifiBleConnectionStateManager by WifiBleConnectionStateManagerImpl(wifiRepository) {

    private val _tag = this.javaClass.simpleName

    private val _uiState = MutableStateFlow(SelectNetworkUiState())
    val uiState: StateFlow<SelectNetworkUiState> = _uiState.asStateFlow()
    private val scanListener = object : WifiNetworkScanListener {
        override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
            updateBleConnectionState(isConnected)
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            updateWifiEnabledState(status)
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            updateNetworkList(list)
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(_tag, "onScanStatusUpdated: ")
        }

        override fun onWifiAlreadyConnected(status: Boolean) {
            Log.i(_tag, "onWifiAlreadyConnected: $status")
        }
    }

    init {
        wifiRepository.registerListener(listener = scanListener)
        wifiRepository.getCachedWifiList()
    }

    private fun updateNetworkList(list: MutableList<ScanResult>) {
        _uiState.update { state ->
            state.copy(networkList = list)
        }
    }

    private fun updateWifiEnabledState(enabled: Boolean) {
        _uiState.update { state -> state.copy(isWifiEnabled = enabled) }
    }

    fun onNetworkSelected(navController: NavController, selectedNetwork: ScanResult) {
        wifiRepository.setSelectedNetwork(selectedNetwork)
        navController.navigate(target = WifiNavigation.EnterPasswordRoute, popCurrent = false)
    }

    fun onBackButtonClicked(navController: NavController) {
        navController.popBackStack()
    }

    fun navigateToNoNetworksFoundScreen(navController: NavController) = with(navController) {
        navigate(target = WifiNavigation.NoNetworksFoundRoute)
    }

    fun onAdvancedButtonClicked(navController: NavController) {
        wifiRepository.setWifiNetwork("", WifiType.WPA, "")     //clean up the saved network

        navController.navigate(
            target = WifiNavigation.AdvancedNetworkSettingsRoute,
            popCurrent = false
        )
    }
}
