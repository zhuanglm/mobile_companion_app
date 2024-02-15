/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtDevicesUiState
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.BtConnectingRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.NoDeviceConnectedRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class BtDevicesViewModel @Inject constructor(
    application: Application,
    private val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(BtDevicesUiState())
    val uiState: StateFlow<BtDevicesUiState> = _uiState.asStateFlow()
    private val btRepoListener = object : BluetoothConnectionRepositoryCallback {
        override fun deviceListReady(deviceList: MutableList<String>) {
            updateDeviceList(deviceList)
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            updateBtEnabledState(enabled)
        }
    }

    init {
        btConnectionRepository.registerListener(btRepoListener)
        btConnectionRepository.setupBtModelListener()
    }

    private fun updateBtEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isBtEnabled = enabled)
        }
    }

    private fun updateDeviceList(devices: List<String>) {
        _uiState.update { state ->
            state.copy(listOfAvailableDevices = devices)
        }
    }

    fun getDeviceList() {
        btConnectionRepository.getMapOfDevices()
    }

    fun navigateToNoDeviceConnectedScreen(navController: NavController) = with(navController) {
        navigate(NoDeviceConnectedRoute)
    }

    fun navigateToBtConnectingScreen(navController: NavController, device: String) {
        btConnectionRepository.connectToDevice(device)

        navController.navigate(BtConnectingRoute)
    }

    fun navigateToUnableToFindESight(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.NoDevicesFoundRoute)
    }

    fun navigateToBtDisabledScreen(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.BtDisabledScreen)
    }
}
