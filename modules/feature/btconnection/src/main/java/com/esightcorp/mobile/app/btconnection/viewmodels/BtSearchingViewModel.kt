package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
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
    private val btRepositoryListener = object : IBtConnectionRepository {
        override fun scanStatus(isScanning: ScanningStatus) {
            Log.i(TAG, "Scan status received from Bluetooth Repository")
            updateBtSearchingState(isScanning)
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
            Log.i(TAG, "deviceListReady: ")
            /*
            TODO: Verify that we should be showing the list once we have at least 1 device, with Nadim
             */
            updateBtSearchingState(ScanningStatus.Success)
        }

        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            Log.i(TAG, "onDeviceConnected: ")
        }
    }

    init {
        btConnectionRepository.registerListener(btRepositoryListener)
        btConnectionRepository.setupBtModelListener()
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
        }

        _uiState.update {
            it.copy(isScanning = status)
        }
    }
}