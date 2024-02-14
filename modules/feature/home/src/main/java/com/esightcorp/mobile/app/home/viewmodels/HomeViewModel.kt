/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import android.util.Log
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.home.repositories.HomeRepositoryListener
import com.esightcorp.mobile.app.home.state.HomeUiState
import com.esightcorp.mobile.app.home.state.HomeUiState.BluetoothState
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    homeRepository: HomeRepository,
) : ESightBaseViewModel(application),
    IBleStateManager by BleStateManagerImpl() {

    private val _tag = this.javaClass.simpleName

    /**
     * Object which is used by the compose UI to track UI State
     */
    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val listener = object : HomeRepositoryListener {
        override fun onBluetoothDisabled() {
            _uiState.update {
                it.copy(bluetoothState = BluetoothState.DISABLED)
            }
        }

        override fun onBluetoothEnabled() {
            _uiState.update {
                it.copy(bluetoothState = BluetoothState.ENABLED)
            }
        }

        override fun onBluetoothDeviceDisconnected() {
            _uiState.update {
                it.copy(bluetoothState = BluetoothState.DISCONNECTED)
            }
        }
    }

    init {
        with(homeRepository) {
            registerListener(listener)

            updateConnectedDevice(getConnectedDevice())
        }
    }

    private fun updateConnectedDevice(device: String?) {
        Log.d(_tag, "updateConnectedDevice: $device")
        device?.let {
            _uiState.update {
                it.copy(connectedDevice = device, bluetoothState = BluetoothState.CONNECTED)
            }
        } ?: _uiState.update { it.copy(bluetoothState = null) }
    }

    fun navigateToWifiCredsOverBt(navController: NavController) = with(navController) {
        navigate(
            target = WifiNavigation.ScanningRoute,
            param = WifiNavigation.ScanningRoute.PARAM_WIFI_CONNECTION
        )
    }

    fun navigateToNoDeviceConnected(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.IncomingRoute)
    }

    fun navigateToBluetoothDisabled(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.BtDisabledScreen)
    }

    fun navigateToShareYourView(navController: NavController) = with(navController) {
        navigate(EShareNavigation.IncomingRoute)
    }

    fun navigateToSettings(navController: NavController) = with(navController) {
        navigate(SettingsNavigation.IncomingRoute)
    }
}
