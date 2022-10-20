package com.esightcorp.mobile.app.btconnection.viewmodels


import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.ScanningStatus
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtConnectionViewModel @Inject constructor(
    application: Application,
    val btConnectionRepository: BtConnectionRepository
): AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(BluetoothUiState())
    val uiState: StateFlow<BluetoothUiState> = _uiState.asStateFlow()
    val btRepositoryListener = object : IBtConnectionRepository{
        override fun scanStatus(isScanning: ScanningStatus) {
            Log.d("", "scanStatus: $isScanning")
            _uiState.update { currentState ->
                currentState.copy(isScanning = isScanning)
            }
            when(isScanning){
                ScanningStatus.Failed -> {
                    Log.d("TAG", "scanStatus: failed")

                }
                ScanningStatus.InProgress -> {
                    Log.d("TAG", "scanStatus: in progress")

                }
                ScanningStatus.Success -> {
                    Log.d("TAG", "scanStatus: success")
                    getDevicesToDisplay()}
                ScanningStatus.Unknown -> {
                    Log.d("TAG", "scanStatus: unknown")

                    getDevicesToDisplay()}
            }
        }

        override fun deviceListReady(deviceList: HashMap<String, Boolean>) {
            Log.d("TAG", "deviceListReady: $deviceList")
            _uiState.update{currentState ->
                currentState.copy(deviceMapCache = deviceList)
            }
        }

    }

    init {
        btConnectionRepository.registerListener(btRepositoryListener)
    }


    fun getBluetoothPermissionsList(): List<String>{
        val PERMISSIONS:List<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            listOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN)
        }
        Log.d("TAG", "getBluetoothPermissionsList: ${PERMISSIONS.first()} ")
        return PERMISSIONS
    }

    fun connectToDevice(device: String){
        btConnectionRepository.connectToDevice(device)
    }

    fun updatePermissionsState(state: Boolean){
        _uiState.update { currentState ->
            currentState.copy(arePermissionsGranted = state)
        }
    }
    fun updateBtEnabledState(state: Boolean){
        _uiState.update { currentState ->
            currentState.copy(isBtEnabled = state)
        }
    }

    private fun updateConnectionStatus(){
        val areWeConnected = btConnectionRepository.checkBtConnectionState()
        _uiState.update { currentState ->
            currentState.copy(btConnectionStatus = areWeConnected)
        }
    }
    fun getDevicesToDisplay(){
        val uiDeviceList: MutableList<String> = mutableListOf()
        val deviceMap = _uiState.value.deviceMapCache
        updateConnectionStatus()

        when(uiState.value.isScanning){
            ScanningStatus.Success -> {
                if(uiState.value.isBtEnabled){
                    deviceMap.forEach {
                        Log.d("TAG", "getDevicesToDisplay: ${deviceMap.keys}")
                        uiDeviceList.add(it.key)
                    }
                }else{
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

            }
            ScanningStatus.Unknown -> {

            }
        }

    }

}