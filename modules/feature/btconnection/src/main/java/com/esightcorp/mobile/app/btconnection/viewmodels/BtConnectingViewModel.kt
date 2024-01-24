/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtConnectingUiState
import com.esightcorp.mobile.app.ui.UI_DELAY_TIMER
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BtConnectingViewModel @Inject constructor(
    application: Application,
    btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {
    private val _tag = this.javaClass.simpleName

    private val _uiState = MutableStateFlow(BtConnectingUiState())
    val uiState: StateFlow<BtConnectingUiState> = _uiState.asStateFlow()

    init {
        with(btConnectionRepository) {
            registerListener(
                object : BluetoothConnectionRepositoryCallback {
                    override fun onDeviceConnected(device: BluetoothDevice?, connected: Boolean?) {
                        Log.d(_tag, "onDeviceConnected: $device -> $connected")
                        updateDeviceInfo(device, connected)
                    }

                    override fun onBtStateUpdate(enabled: Boolean) {
                        updateBtEnabledState(enabled)
                    }
                },
            )

            setupBtModelListener()
            checkBtEnabledStatus()
        }
    }

    fun navigateToConnectedScreen(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.BtConnectedRoute)
    }

    fun navigateToUnableToConnectScreen(navController: NavController) {
        navController.navigate(BtConnectionNavigation.UnableToConnectRoute)
    }

    @SuppressLint("MissingPermission")
    private fun updateDeviceInfo(device: BluetoothDevice?, connected: Boolean?) {
        val updateAction = {
            _uiState.update { state ->
                state.copy(
                    didDeviceConnect = connected,
                    deviceName = device?.name,
                    deviceAddress = device?.address,
                )
            }
        }

        when (connected) {
            // Delay a bit to allow user read the screen info
            true -> viewModelScope.launch(Dispatchers.IO) {
                delay(UI_DELAY_TIMER)

                updateAction.invoke()
            }

            // No need timer here as when connection fail, it really takes time already
            else -> updateAction.invoke()
        }
    }

    private fun updateBtEnabledState(enabled: Boolean) = _uiState.update { state ->
        state.copy(isBtEnabled = enabled)
    }
}
