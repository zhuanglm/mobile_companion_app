package com.esightcorp.mobile.app.btconnection.viewmodels


import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
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

private const val TAG = "BtConnectionViewModel"

@HiltViewModel
class BtConnectionViewModel @Inject constructor(
    application: Application, val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {


    /**
     * Object which is used by the compose UI to track UI State
     */
    private var _uiState = MutableStateFlow(BluetoothUiState())
    val uiState: StateFlow<BluetoothUiState> = _uiState.asStateFlow()

    /**
     * Methods to interact with the UI state StateFlow object
     */


    fun connectToDevice(device: String) {
        btConnectionRepository.connectToDevice(device)
    }


    fun updateBtEnabledState(state: Boolean) {
        if (state) {
            btConnectionRepository.triggerBleScan()
        }
        _uiState.update { currentState ->
            currentState.copy(isBtEnabled = state)
        }
    }


    /**
     * Interface to receive callbacks from the bluetooth repository
     */
    val btRepositoryListener = object : IBtConnectionRepository {
        override fun scanStatus(isScanning: ScanningStatus) {
            /*Log.d("", "scanStatus: $isScanning")
            _uiState.update { currentState ->
                currentState.copy(isScanning = isScanning)
            }
            when (isScanning) {
                ScanningStatus.Failed -> {
                    Log.d("TAG", "scanStatus: failed")
                }
                ScanningStatus.InProgress -> {
                    Log.d("TAG", "scanStatus: in progress")
                }
                ScanningStatus.Success -> {
                    Log.d("TAG", "scanStatus: success")
                    uiDeviceList()
                }
                ScanningStatus.Unknown -> {
                    Log.d("TAG", "scanStatus: unknown")
                    uiDeviceList()
                }
            }*/
        }

        override fun deviceListReady(deviceList: MutableList<String>) {
           /* Log.d("TAG", "deviceListReady: $deviceList")
            _uiState.update { currentState ->
                currentState.copy(deviceMapCache = deviceList)
            }*/
        }

        @SuppressLint("MissingPermission")
        override fun onDeviceConnected(device: BluetoothDevice) {
            Log.d(TAG, "onDeviceConnected: ${device.name}")
            _uiState.update { currentState ->
                currentState.copy(
                    getConnectedDevice = device.name.trim(), btConnectionStatus = true
                )
            }
            Log.e(TAG, "onDeviceConnected: ${_uiState.value.getConnectedDevice}")

        }

    }

    /**
     * First constructor is init{}
     */

    init {
        btConnectionRepository.registerListener(btRepositoryListener)
        btConnectionRepository.setupBtModelListener()
    }


    /**
     * Generate list of ble devices to show to the user
     */

  /*  fun uiDeviceList() {
        val uiDeviceList: MutableList<String> = mutableListOf()
        val deviceList = _uiState.value.deviceMapCache
        when (uiState.value.isScanning) {
            ScanningStatus.Success -> {
                if (uiState.value.isBtEnabled) {
                    deviceList.forEach {
                        Log.d("TAG", "getDevicesToDisplay: ${it}")
                        uiDeviceList.add(it)
                    }
                } else {
                    uiDeviceList.add("Bluetooth needs to be enabled")
                }
                _uiState.update { currentState ->
                    currentState.copy(listOfAvailableDevices = uiDeviceList)
                }

            }
            ScanningStatus.Failed -> {
                uiDeviceList.add("ERROR SCAN FAILED ERROR ")
                _uiState.update { currentState ->
                    currentState.copy(listOfAvailableDevices = uiDeviceList)
                }
            }
            ScanningStatus.InProgress -> {
                Log.d(TAG, "getDevicesToDisplay: Scan in progress")
            }
            ScanningStatus.Unknown -> {
                Log.d(TAG, "getDevicesToDisplay: Scan status UNKNOWN")
            }
        }

    }*/


    /**
     * Refresh list of ble devices to show to the user
     */

    //TODO: Need this to happen when the user swipes down on list
    fun refreshUiDeviceList() {
        btConnectionRepository.triggerBleScan()
    }


}