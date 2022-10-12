package com.esightcorp.mobile.app.btconnection.viewmodels

import android.Manifest
import android.Manifest.permission.BLUETOOTH
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.IBtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiEvent
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BtConnectionViewModel @Inject constructor(
    application: Application,
    val btConnectionRepository: BtConnectionRepository
): AndroidViewModel(application) {

    private var _uiState = mutableStateOf(BluetoothUiState())
    val uiState: State<BluetoothUiState> = _uiState


    fun onEvent(event: BluetoothUiEvent){
        when (event){
            is BluetoothUiEvent.BluetoothConnected -> {
                _uiState.value.copy(
                    isBluetoothConnected = event.isConnected
                )
            }
            is BluetoothUiEvent.BluetoothEnabled -> {
                _uiState.value.copy(
                    isBluetoothEnabled = event.isEnabled
                )
            }
            is BluetoothUiEvent.PermissionsGranted -> {
                _uiState.value.copy(
                    arePermissionsGranted = event.areGranted
                )
            }
            is BluetoothUiEvent.BluetoothScanResponse -> {
                _uiState.value.copy(
                    listOfAvailableDevices = event.bluetoothDeviceList
                )
            }
            is BluetoothUiEvent.BluetoothReadyToGo -> {
                btConnectionRepository.getListOfDevices()
            }
        }
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

    fun getBtConnectionState(){
        btConnectionRepository.checkBtConnectionState()
    }




}