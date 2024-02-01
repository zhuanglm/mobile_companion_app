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
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionStatus
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
) : ESightBaseViewModel(application) {

    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiSearchingUiState())
    val uiState: StateFlow<WifiSearchingUiState> = _uiState.asStateFlow()

    private val repoListener = object : WifiNetworkScanListener {
        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            _uiState.update { it.copy(scanResults = list) }
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
        with(repository) {
            // First, reset the wifiFlow
            // Note: the actual flow will be initialized later from the composable
            setWifiFlow(null)

            registerListener(repoListener)
        }
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

    private fun updateWifiStatusState(status: Boolean) {
        _uiState.update { state ->
            state.copy(
                wifiConnectionStatus =
                if (status) WifiConnectionStatus.CONNECTED
                else WifiConnectionStatus.DISCONNECTED
            )
        }
    }

    /**
     * Navigating to the next screen depends on the scan status:
     * * If `WifiCache.WifiFlow.QrFlow` and could not find any wifi network, navigate to manual input screen
     * * If having result, go to select-network screen
     * * else, go to no-result-found screen
     */
    fun onWifiScanCompletedCallback(navController: NavController) {
        val routeTarget = with(_uiState.value) {
            when (scanningStatus) {
                ScanningStatus.Failed -> WifiNavigation.NoNetworksFoundRoute
//                ScanningStatus.Failed,
                ScanningStatus.Success -> when (scanResults.isNotEmpty()) {
                    true -> WifiNavigation.SelectNetworkRoute

                    false -> {
                        Log.i(_tag, "scanResults is empty!")

                        when (repository.wifiFlow) {
                            WifiCache.WifiFlow.QrFlow -> WifiNavigation.AdvancedNetworkSettingsRoute

                            else -> WifiNavigation.NoNetworksFoundRoute
                        }
                    }
                }

                // should not reach here
                else -> null
            }
        }

        routeTarget?.let { navController.navigate(target = it) }
    }

    fun navigateToWifiAlreadyConnected(navController: NavController) {
        navController.navigate(target = WifiNavigation.AlreadyConnectedRoute)
    }

    fun onCancelClicked(navController: NavController) {
        Log.i(_tag, "onCancelClicked - flow: ${repository.wifiFlow}")
        repository.cancelWifiScan()
        navController.popBackStack()
    }

    @Synchronized
    fun setWifiFlow(flow: String?) {
        repository.setWifiFlow(flow)

        // start job according to flow type
        flow?.let {
            when (it) {
                WifiNavigation.ScanningRoute.PARAM_WIFI_CONNECTION ->
                    repository.readWifiConnectionStatus()

                //bluetooth and QR
                else -> {
                    if (!_uiState.value.isWifiEnabled) {
                        Log.e(_tag, "setWifiFlow: skipped scanning as wifi is off!")
                        return@let
                    }

                    Log.i(_tag, "setWifiFlow: start scan")
                    repository.startWifiScan()
                }
            }
        }
    }
}
