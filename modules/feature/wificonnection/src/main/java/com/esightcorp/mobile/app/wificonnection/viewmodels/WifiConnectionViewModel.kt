package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepoListener
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG  = "WifiConnectionViewModel"

@HiltViewModel
class WifiConnectionViewModel @Inject constructor(
    application: Application,
  val wifiConnectionRepository: WifiConnectionRepository
): AndroidViewModel(application) {


    /**
     * Holds ui states -> see WifiConnectionUiState for the full list of states and what they are
     */
    private var _uiState = MutableStateFlow(WifiConnectionUiState())
    val uiState: StateFlow<WifiConnectionUiState> = _uiState.asStateFlow()
    private val wifiRepoListener = object : WifiConnectionRepoListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.e(TAG, "onBluetoothNotConnected: Bluetooth needs to be connected to send a message " )
            updateQrCodeButtonVisibility(true)
            _uiState.update { state ->
                state.copy(bluetoothConnected = status)
            }
        }

        override fun onWifiConnected(success: Boolean) {
            Log.e(TAG, "onWifiConnected: ")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            _uiState.update { state ->
                state.copy(networkList = list)
            }
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.d(TAG, "onScanStatusUpdated: ")
        }
    }

    init {
        wifiConnectionRepository.registerListener(wifiRepoListener)
    }

    fun updateCurrentSelectedNetwork(result: ScanResult){
        _uiState.update { state ->
            state.copy(currentSelectedNetwork = result)
        }
        wifiConnectionRepository.setSelectedNetwork(result)

    }

    fun updateSsid(ssid: String){
        _uiState.update { state ->
            state.copy(ssid = ssid)
        }
    }



    fun updateQrCodeButtonVisibility(boolean: Boolean){
        _uiState.update { state ->
            state.copy(qrCodeButtonVisibility = boolean)
        }
    }

    fun updatePermissionsGranted(boolean: Boolean){
        _uiState.update { state ->
            state.copy(arePermissionsGranted = boolean)
        }

    }



    fun startWifiScan(){
        wifiConnectionRepository.startWifiScan()
    }









}