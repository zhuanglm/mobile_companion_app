package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.SelectNetworkUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SelectNetworkViewModel @Inject constructor(
    application: Application,
  val  wifiRepository: WifiConnectionRepository
): AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(SelectNetworkUiState())
    val uiState: StateFlow<SelectNetworkUiState> = _uiState.asStateFlow()
    val TAG = "SelectNetworkViewModel"
    val scanListener = object : WifiNetworkScanListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {
            Log.e(TAG, "onBluetoothStatusUpdate: ", )
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            updateWifiEnabledState(status)
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            list.forEach {
                Log.d("SelectNetworkViewModel", "onNetworkListUpdated: ${it.SSID}")

            }
            updateNetworkList(list)
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
            Log.i(TAG, "onScanStatusUpdated: ")
        }
    }

    init {
        wifiRepository.registerListener(listener = scanListener)
        wifiRepository.getCachedWifiList()
    }

    fun updateNetworkList(list: MutableList<ScanResult>){
        _uiState.update {state ->
            state.copy(networkList = list)
        }
    }

    private fun updateWifiEnabledState(enabled: Boolean){
        _uiState.update { state -> state.copy(isWifiEnabled = enabled) }

    }

    fun selectNetwork(network:ScanResult){
        wifiRepository.setSelectedNetwork(network)
    }

    fun navigateToPasswordScreen(navController: NavController){
        navController.navigate(WifiConnectionScreens.EnterPasswordRoute.route)
    }

    fun onBackButtonClicked(navController: NavController){
        navController.popBackStack("home_first", false)
    }

    fun navigateToNoNetworksFoundScreen(navController: NavController){
        navController.navigate(WifiConnectionScreens.NoNetworksFoundRoute.route)
    }

    fun onAdvancedButtonClicked(navController: NavController){
        navController.navigate(WifiConnectionScreens.AdvancedNetworkSettingsRoute.route)
    }
}