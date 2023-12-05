package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.NoDevicesFoundUiState
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NoDevicesFoundViewModel @Inject constructor(
    application: Application,
    btConnectionRepository: BtConnectionRepository,
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(NoDevicesFoundUiState())
    val uiState: StateFlow<NoDevicesFoundUiState> = _uiState.asStateFlow()
    private val listener = object : BluetoothConnectionRepositoryCallback {
        override fun scanStatus(isScanning: ScanningStatus) {
            //unused by this composable
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            //unused by this composable
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            //unused by this composable
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            updateIsBtEnabledState(enabled)
        }
    }

    init {
        btConnectionRepository.registerListener(listener)
        btConnectionRepository.setupBtModelListener()
    }

    private fun updateIsBtEnabledState(enabled: Boolean) {
        _uiState.update { state -> state.copy(isBtEnabled = enabled) }
    }


    fun navigateToNoDevicesConnectedScreen(navController: NavController) {
        navController.navigate(BtConnectionNavigation.NoDeviceConnectedRoute)
    }

    fun navigateToSearchingScreen(navController: NavController) {
        navController.navigate(BtConnectionNavigation.BtSearchingRoute)
    }

    fun navigateToUnableToConnectScreen(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.UnableToConnectRoute)
    }

    fun onBluetoothDisabled(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.BtDisabledScreen)
    }
}
