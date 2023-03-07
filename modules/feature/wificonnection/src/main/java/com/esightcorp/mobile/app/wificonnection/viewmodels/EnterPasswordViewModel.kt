package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepoListener
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class EnterPasswordViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
): AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(WifiCredentialsUiState())
    val uiState: StateFlow<WifiCredentialsUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController
    val TAG = "EnterPasswordViewModel"

    val repoListener = object : WifiConnectionRepoListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.i(TAG, "onBluetoothStatusUpdate: ")
        }

        override fun onWifiConnected(success: Boolean) {
            Log.i(TAG, "onWifiConnected: ")
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.i(TAG, "onNetworkListUpdated: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(TAG, "onScanStatusUpdated: ")
        }
    }

    init {
        repository.registerListener(repoListener)
    }

    fun setNavController(navController: NavController){
        this.navController = navController
    }

    fun updatePassword(password:String){
        Log.d("WifiCredentialsViewModel", "updatePassword: $password")
        _uiState.update { state ->
            state.copy(password = password)
        }
    }

    fun updateWifiType(type:String){
        Log.d("WifiCredentialsViewModel", "updateWifiType: $type")
        _uiState.update { state ->
            state.copy(wifiType = type)
        }
    }

    fun wifiPasswordSubmitted(){
        _uiState.update { state ->
            state.copy(passwordSubmitted = true)
        }
        sendWifiCredsViaBluetooth()
        navigateToConnectingScreen()
    }

    fun wifiTypeSubmitted(){
        _uiState.update { state ->
            state.copy(wifiTypeSubmitted = true)
        }
        Log.d("WifiCredentialsViewModel", "wifiTypeSubmitted: ")
    }

    fun sendWifiCredsViaBluetooth(){
        Log.d("WifiCredentialsViewModel", "sendWifiCredsViaBluetooth: ")
        repository.sendWifiCreds(_uiState.value.password, _uiState.value.wifiType)
    }

    private fun navigateToConnectingScreen(){
        if (this::navController.isInitialized){
            navController.navigate(WifiConnectionScreens.ConnectingRoute.route)
        }
    }




}