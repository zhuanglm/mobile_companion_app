package com.esightcorp.mobile.app.btconnection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtConnectingUiState
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtConnectingViewModel @Inject constructor(
    application: Application,
    btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {
    private val TAG = "BtConnectingViewModel"
    private var _uiState = MutableStateFlow(BtConnectingUiState())
    val uiState: StateFlow<BtConnectingUiState> = _uiState.asStateFlow()
    private val listener = object : BluetoothConnectionRepositoryCallback {
        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.d(TAG, "onDeviceConnected: $device")
            updateDeviceInfo(device, connected)
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            updateBtEnabledState(enabled)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateDeviceInfo(device: BluetoothDevice, connected: Boolean) {
        _uiState.update { state ->
            state.copy(
                didDeviceConnect = connected,
                deviceName = device.name,
                deviceAddress = device.address,
            )
        }
    }

    init {
        btConnectionRepository.registerListener(listener)
        btConnectionRepository.setupBtModelListener()
        btConnectionRepository.checkBtEnabledStatus()
    }

    fun navigateToConnectedScreen(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.BtConnectedRoute)
    }

    fun navigateToUnableToConnectScreen(navController: NavController) {
        navController.graph.forEach {
            Log.d(TAG, "navigateToUnableToConnectScreen: ${it.route}")
        }
        navController.navigate(BtConnectionNavigation.UnableToConnectRoute)
    }

    private fun updateBtEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isBtEnabled = enabled)
        }
    }


}