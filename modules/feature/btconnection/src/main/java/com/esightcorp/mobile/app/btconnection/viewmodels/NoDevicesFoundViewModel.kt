package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.NoDevicesFoundUiState
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
        navController.navigate(BtConnectionScreens.NoDevicesConnectedRoute.route) {
            popUpTo(BtConnectionScreens.NoDevicesFoundRoute.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToSearchingScreen(navController: NavController) {
        navController.navigate(BtConnectionScreens.BtSearchingRoute.route) {
            popUpTo(BtConnectionScreens.NoDevicesFoundRoute.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToUnableToConnectScreen(navController: NavController) = with(navController) {
        navigate(BtConnectionScreens.UnableToConnectRoute.route) {
            popUpTo(BtConnectionScreens.NoDevicesFoundRoute.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun onBluetoothDisabled(navController: NavController) {
        navController.navigate(BtConnectionScreens.BtDisabledScreen.route) {
            popUpTo(BtConnectionScreens.NoDevicesConnectedRoute.route) { inclusive = false }
            launchSingleTop = true
        }
    }
}
