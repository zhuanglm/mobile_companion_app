package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.WifiCache
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class EnterPasswordViewModel @Inject constructor(
    application: Application, val repository: WifiConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(WifiCredentialsUiState())
    val uiState: StateFlow<WifiCredentialsUiState> = _uiState.asStateFlow()
    val TAG = "EnterPasswordViewModel"

    val scanListener = object : WifiNetworkScanListener {
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.i(TAG, "onBluetoothStatusUpdate: ")
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.i(TAG, "onNetworkListUpdated: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(TAG, "onScanStatusUpdated: ")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            Log.i(TAG, "onWifiStatusUpdate: ")
        }
    }


    init {
        repository.registerListener(scanListener)
        updateWifiFlow(repository.getCurrentWifiFlow())
    }

    fun updatePassword(password: String) {
        Log.d("WifiCredentialsViewModel", "updatePassword: $password")
        _uiState.update { state ->
            state.copy(password = password)
        }
    }
    fun updateWifiFlow(flow: WifiCache.WifiFlow){
        _uiState.update { state ->
            state.copy(wifiFlow = flow)
        }
    }

    fun wifiPasswordSubmitted(navController: NavController) {
        _uiState.update { state ->
            state.copy(passwordSubmitted = true)
        }
        if(_uiState.value.wifiFlow == WifiCache.WifiFlow.BluetoothFlow){
            sendWifiCredsViaBluetooth()
            navigateToConnectingScreen(navController)
        }else if(_uiState.value.wifiFlow == WifiCache.WifiFlow.QrFlow){
            navigateToQrScreen(navController)
        }
        
    }

    fun onAdvancedButtonPressed(navController: NavController) {
        navController.navigate(WifiConnectionScreens.AdvancedNetworkSettingsRoute.route)
    }

    fun onBackButtonPressed(navController: NavController) {
        navController.popBackStack()
    }

    fun sendWifiCredsViaBluetooth() {
        Log.d("WifiCredentialsViewModel", "sendWifiCredsViaBluetooth: ")
        repository.sendWifiCreds(_uiState.value.password, _uiState.value.wifiType)
    }

    private fun navigateToConnectingScreen(navController: NavController) {
        Log.i(TAG, "navigateToConnectingScreen: Bluetooth route selected")
        navController.navigate(WifiConnectionScreens.ConnectingRoute.route)
    }
    
    private fun navigateToQrScreen(navController: NavController){
        Log.i(TAG, "navigateToQrScreen: QR Route selected")
        navController.navigate(WifiConnectionScreens.WifiQRCodeRoute.route)
    }


}