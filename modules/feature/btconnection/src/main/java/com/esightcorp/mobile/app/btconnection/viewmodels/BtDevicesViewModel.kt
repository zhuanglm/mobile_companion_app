package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtDevicesUiState
import com.esightcorp.mobile.app.ui.components.toStringList
import com.esightcorp.mobile.app.utils.ScanningStatus
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
    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(BtDevicesUiState())
    val uiState: StateFlow<BtDevicesUiState> = _uiState.asStateFlow()
    private val btRepoListener = object : BluetoothConnectionRepositoryCallback {
        override fun scanStatus(isScanning: ScanningStatus) {
            //unused in this composable
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            updateDeviceList(deviceList)
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.d(_tag, "onDeviceConnected: ")
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

    fun navigateToNoDeviceConnectedScreen(navController: NavController) {
//        Log.w(_tag, "Current back-stack:\n${navController.currentBackStack.value.toStringList()}")
        with(navController) {
            navigate(BtConnectionScreens.NoDevicesConnectedRoute.route) {
                popUpTo(BtConnectionScreens.BtDevicesScreen.route) { inclusive = true }
            }
        }
    }

    fun navigateToBtConnectingScreen(navController: NavController, device: String) {
        btConnectionRepository.connectToDevice(device)
        navController.navigate(BtConnectionScreens.BTConnectingRoute.route)
    }

    fun navigateToUnableToFindESight(navController: NavController) = with(navController) {
        navigate(BtConnectionScreens.NoDevicesFoundRoute.route) {
            popUpTo(BtConnectionScreens.BtDevicesScreen.route) { inclusive = true }
        }
    }
}
