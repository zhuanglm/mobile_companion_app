package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtDevicesUiState
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
    val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {
    private val TAG = "BtDevicesViewModel"

    private var _uiState = MutableStateFlow(BtDevicesUiState())
    val uiState: StateFlow<BtDevicesUiState> = _uiState.asStateFlow()
    val btRepoListener = object : IBtConnectionRepository {
        override fun scanStatus(isScanning: ScanningStatus) {
            Log.d(TAG, "scanStatus: $isScanning")
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            Log.d(TAG, "deviceListReady: ")
            updateDeviceList(deviceList)
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.d(TAG, "onDeviceConnected: ")
        }
    }

    init {
        btConnectionRepository.registerListener(btRepoListener)
        btConnectionRepository.setupBtModelListener()
    }

    fun updateDeviceList(devices: List<String>) {
        _uiState.update { state ->
            state.copy(listOfAvailableDevices = devices)
        }
    }

    fun getDeviceList() {
        Log.i(TAG, "getDeviceList: ")
        btConnectionRepository.getMapOfDevices()
    }

    fun navigateToNoDeviceConnectedScreen(navController: NavController) {
        Log.d(TAG, "navigateToNoDeviceConnectedScreen: ")
        navController.navigate(BtConnectionScreens.BtConnectionHomeScreen.route)
    }

    fun navigateToBtConnectingScreen(navController: NavController, device: String){
        Log.d(TAG, "navigateToBtConnectingScreen: ")
        btConnectionRepository.connectToDevice(device)
        navController.navigate(BtConnectionScreens.BTConnectingRoute.route)
    }
}