package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.state.BtSearchingUiState
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class BtSearchingViewModel @Inject constructor(
    application: Application, private val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {
    private val TAG = "BtSearchingViewModel"
    private var _uiState = MutableStateFlow(BtSearchingUiState())
    val uiState: StateFlow<BtSearchingUiState> = _uiState.asStateFlow()
    private val btRepositoryListener = object : BluetoothConnectionRepositoryCallback {
        override fun scanStatus(isScanning: ScanningStatus) {
            Log.i(TAG, "Scan status received from Bluetooth Repository")
            updateBtSearchingState(isScanning)
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            Log.i(TAG, "deviceListReady: ")
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.i(TAG, "onDeviceConnected: ")
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            _uiState.update {
                it.copy(isBtEnabled = enabled)
            }
        }
    }

    init {
        btConnectionRepository.registerListener(btRepositoryListener)
        btConnectionRepository.setupBtModelListener()
        btConnectionRepository.resetBtDeviceList()
    }


    fun triggerScan() {
        btConnectionRepository.triggerBleScan()
    }

    fun updateBtSearchingState(status: ScanningStatus) {
        when (status) {
            ScanningStatus.Failed -> {
                Log.d(TAG, "updateBtSearchingState: ${ScanningStatus.Failed}")
            }
            ScanningStatus.InProgress -> {
                Log.d(TAG, "updateBtSearchingState: ${ScanningStatus.InProgress}")
            }
            ScanningStatus.Success -> {
                Log.d(TAG, "updateBtSearchingState: ${ScanningStatus.Success}")
            }
            ScanningStatus.Unknown -> {
                Log.d(TAG, "updateBtSearchingState: ${ScanningStatus.Unknown}")
            }

            ScanningStatus.Cancelled -> {
                Log.d(TAG, "updateBtSearchingState: ${ScanningStatus.Cancelled}")
            }
        }

        _uiState.update {
            it.copy(isScanning = status)
        }
    }

    fun onCancelButtonClicked(navController: NavController){
        //navigate back to the 'no devices connected screen'
        navController.navigate(BtConnectionScreens.NoDevicesConnectedRoute.route){
            popUpTo(BtConnectionScreens.NoDevicesConnectedRoute.route){
                inclusive = false
            }
            launchSingleTop = true
        }
        //cleanup bluetooth scanning
        btConnectionRepository.cancelBleScan()
        
    }
}