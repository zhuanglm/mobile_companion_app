package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.storage.WifiCache
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
    application: Application,
    private val repository: WifiConnectionRepository,
) : AndroidViewModel(application) {
    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiCredentialsUiState())
    val uiState: StateFlow<WifiCredentialsUiState> = _uiState.asStateFlow()

    private val scanListener = object : WifiNetworkScanListener {
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.i(_tag, "onBluetoothStatusUpdate: ")
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.i(_tag, "onNetworkListUpdated: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(_tag, "onScanStatusUpdated: ")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            Log.i(_tag, "onWifiStatusUpdate: ")
        }

        override fun onWifiAlreadyConnected(status: Boolean) {
            Log.i(_tag, "onWifiAlreadyConnected: $status")
        }
    }

    init {
        repository.registerListener(scanListener)
        updateWifiFlow(repository.wifiFlow)
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }

    fun updatePassword(password: String) {
        Log.d("WifiCredentialsViewModel", "updatePassword: $password")
        _uiState.update { state ->
            state.copy(
                password = password,
                isPasswordValid = password.length >= MIN_PASSWORD_LENGTH,
            )
        }
    }

    private fun updateWifiFlow(flow: WifiCache.WifiFlow) {
        _uiState.update { state ->
            state.copy(wifiFlow = flow)
        }
    }

    fun wifiPasswordSubmitted(navController: NavController) {
        _uiState.update { state ->
            state.copy(passwordSubmitted = true)
        }
        repository.setWifiPassword(_uiState.value.password)
        if (_uiState.value.wifiFlow == WifiCache.WifiFlow.BluetoothFlow) {
            sendWifiCredsViaBluetooth()
            navigateToConnectingScreen(navController)
        } else if (_uiState.value.wifiFlow == WifiCache.WifiFlow.QrFlow) {
            navigateToQrScreen(navController)
        }
    }

    fun onAdvancedButtonPressed(navController: NavController) {
        navController.navigate(WifiConnectionScreens.AdvancedNetworkSettingsRoute.route)
    }

    fun onBackButtonPressed(navController: NavController) {
        navController.popBackStack()
    }

    private fun sendWifiCredsViaBluetooth() {
        Log.d("WifiCredentialsViewModel", "sendWifiCredsViaBluetooth: ")
        repository.sendWifiCreds(_uiState.value.password, _uiState.value.wifiType)
    }

    private fun navigateToConnectingScreen(navController: NavController) {
        Log.i(_tag, "navigateToConnectingScreen: Bluetooth route selected")
        navController.navigate(WifiConnectionScreens.ConnectingRoute.route)
    }

    private fun navigateToQrScreen(navController: NavController) {
        Log.i(_tag, "navigateToQrScreen: QR Route selected")
        navController.navigate(WifiConnectionScreens.WifiQRCodeRoute.route)
    }
}
