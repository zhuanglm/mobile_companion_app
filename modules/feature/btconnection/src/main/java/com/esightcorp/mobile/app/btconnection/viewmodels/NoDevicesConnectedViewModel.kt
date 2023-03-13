package com.esightcorp.mobile.app.btconnection.viewmodels


import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class NoDevicesConnectedViewModel @Inject constructor(
    application: Application, private val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {

    /**
     * Object which is used by the compose UI to track UI State
     */
    private var _uiState = MutableStateFlow(BluetoothUiState())
    val uiState: StateFlow<BluetoothUiState> = _uiState.asStateFlow()

    /**
     * Methods to interact with the UI state StateFlow object
     */

    fun updateBtEnabledState(state: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isBtEnabled = state)
        }
    }

    fun updateBtConnectedState(state: Boolean, device: String){
        _uiState.update { currentState ->
            currentState.copy(btConnectionStatus = state, connectedDevice = device)
        }
    }


    /**
     * Interface to receive callbacks from the bluetooth repository
     */
    private val btRepositoryListener = object : IBtConnectionRepository {
        override fun scanStatus(isScanning: ScanningStatus) {
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
        }

        @SuppressLint("MissingPermission")
        override fun onDeviceConnected(device: BluetoothDevice, connected: Boolean) {
            updateBtConnectedState(connected, device.name)
        }

    }

    /**
     * First constructor is init{}
     */

    init {
        btConnectionRepository.registerListener(btRepositoryListener)
        btConnectionRepository.setupBtModelListener()
    }




}