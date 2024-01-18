package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class WifiConnectionViewModel @Inject constructor(
    application: Application,
    private val wifiConnectionRepository: WifiConnectionRepository
) : AndroidViewModel(application) {

    private val _tag = this.javaClass.simpleName

    /**
     * Holds ui states -> see WifiConnectionUiState for the full list of states and what they are
     */
    private var _uiState = MutableStateFlow(WifiConnectionUiState())
    val uiState: StateFlow<WifiConnectionUiState> = _uiState.asStateFlow()
    private val wifiRepoListener = object : WifiNetworkScanListener {
        override fun onBleConnectionStatusUpdate(isConnected: Boolean) {
            Log.e(
                _tag,
                "onBluetoothNotConnected: Bluetooth needs to be connected to send a message "
            )
            updateQrCodeButtonVisibility(true)
            _uiState.update { state -> state.copy(bluetoothConnected = isConnected) }
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            _uiState.update { state -> state.copy(networkList = list) }
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.d(_tag, "onScanStatusUpdated: ")
        }
    }

    init {
        wifiConnectionRepository.registerListener(wifiRepoListener)
    }

    fun updateCurrentSelectedNetwork(result: ScanResult) {
        _uiState.update { state ->
            state.copy(currentSelectedNetwork = result)
        }
        wifiConnectionRepository.setSelectedNetwork(result)

    }

    fun updateSsid(ssid: String) {
        _uiState.update { state ->
            state.copy(ssid = ssid)
        }
    }


    fun updateQrCodeButtonVisibility(boolean: Boolean) {
        _uiState.update { state ->
            state.copy(qrCodeButtonVisibility = boolean)
        }
    }

    fun updatePermissionsGranted(boolean: Boolean) {
        _uiState.update { state ->
            state.copy(arePermissionsGranted = boolean)
        }

    }


    fun startWifiScan() {
        wifiConnectionRepository.startWifiScan()
    }


}