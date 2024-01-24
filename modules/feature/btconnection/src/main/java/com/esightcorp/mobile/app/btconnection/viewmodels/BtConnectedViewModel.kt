/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtConnectedUiState
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtConnectedViewModel @Inject constructor(
    application: Application,
    private val repository: BtConnectionRepository,
) : ESightBaseViewModel(application) {

    private var _uiState = MutableStateFlow(BtConnectedUiState())
    val uiState: StateFlow<BtConnectedUiState> = _uiState.asStateFlow()

    private val listener = object : BluetoothConnectionRepositoryCallback {
        override fun onBtStateUpdate(enabled: Boolean) {
            updateIsBtEnabledState(enabled)
        }
    }

    init {
        repository.registerListener(listener)
        getBluetoothDevice()
    }

    fun gotoNoDeviceConnectedScreen(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.NoDeviceConnectedRoute)
    }

    @SuppressLint("MissingPermission")
    private fun getBluetoothDevice() {
        val device = repository.getConnectedDevice()
        updateUiState(device?.name, device?.address)

    }

    private fun updateUiState(name: String?, address: String?) {
        _uiState.update { state ->
            state.copy(deviceName = name, deviceAddress = address)
        }
    }

    private fun updateIsBtEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isBtEnabled = enabled)
        }
    }
}
