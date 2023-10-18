package com.esightcorp.mobile.app.btconnection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.state.BtConnectedUiState
import com.esightcorp.mobile.app.utils.ScanningStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class BtConnectedViewModel @Inject constructor(
    application: Application, private val repository: BtConnectionRepository
) : AndroidViewModel(application) {

    private val TAG = "BtConnectedViewModel"
    private var _uiState = MutableStateFlow(BtConnectedUiState())
    val uiState: StateFlow<BtConnectedUiState> = _uiState.asStateFlow()
    val listener = object : BluetoothConnectionRepositoryCallback{
        override fun scanStatus(isScanning: ScanningStatus){
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
        repository.registerListener(listener)
        getBluetoothDevice()
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

    private fun updateIsBtEnabledState(enabled: Boolean){
        _uiState.update { state ->
            state.copy(isBtEnabled = enabled)
        }
    }


}