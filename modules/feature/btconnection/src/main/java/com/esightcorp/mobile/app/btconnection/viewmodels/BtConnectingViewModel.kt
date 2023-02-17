package com.esightcorp.mobile.app.btconnection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BtConnectingUiState
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtConnectingViewModel @Inject constructor(
    application: Application,
    val btConnectionRepository: BtConnectionRepository
): AndroidViewModel(application) {
    private val TAG = "BtConnectingViewModel"
    private var _uiState = MutableStateFlow(BtConnectingUiState())
    val uiState: StateFlow<BtConnectingUiState> = _uiState.asStateFlow()
    val listener = object : IBtConnectionRepository{
        override fun scanStatus(isScanning: ScanningStatus) {
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.d(TAG, "onDeviceConnected: $device")
            updateDeviceInfo(device, connected)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateDeviceInfo(device: BluetoothDevice, connected: Boolean){
         _uiState.update { state ->
             state.copy(didDeviceConnect = true, deviceName = device.name, deviceAddress = device.address)
         }
    }

    init {
        btConnectionRepository.registerListener(listener)
        btConnectionRepository.setupBtModelListener()
    }

    fun navigateToConnectedScreen(navController: NavController, deviceName: String, address: String){
        navController.navigate(BtConnectionScreens.BtConnectedRoute.route + "/{$address}/{$deviceName}")
    }

}