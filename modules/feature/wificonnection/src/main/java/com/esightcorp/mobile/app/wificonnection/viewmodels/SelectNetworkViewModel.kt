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
    val repoListener = object : WifiConnectionRepoListener{
        override fun onBluetoothStatusUpdate(status: Boolean) {
        }

        override fun onWifiConnected(success: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            list.forEach {
                Log.d("SelectNetworkViewModel", "onNetworkListUpdated: ${it.SSID}")

            }
            updateNetworkList(list)
        }

        override fun onScanStatusUpdated(status: ScanningStatus) {
        }
    }

    init {
        wifiRepository.registerListener(listener = repoListener)
        wifiRepository.getCachedWifiList()
    }

    fun updateNetworkList(list: MutableList<ScanResult>){
        _uiState.update {state ->
            state.copy(networkList = list)
        }
    }

    fun selectNetwork(network:ScanResult){
        wifiRepository.setSelectedNetwork(network)
    }

    fun navigateToPasswordScreen(navController: NavController){
        navController.navigate(WifiConnectionScreens.WifiCredentialsScreen.route)
    }
}