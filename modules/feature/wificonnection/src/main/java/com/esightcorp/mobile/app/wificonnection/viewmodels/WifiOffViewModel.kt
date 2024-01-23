package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiNetworkScanListener
import com.esightcorp.mobile.app.wificonnection.state.WifiOffUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WifiOffViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
): ESightBaseViewModel(application) {
    private val TAG = "WifiOffViewModel"
    private var _uiState = MutableStateFlow(WifiOffUiState())
    val uiState: StateFlow<WifiOffUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController
    private val listener = object : WifiNetworkScanListener {

        override fun onBluetoothStatusUpdate(status: Boolean) {
            _uiState.value = _uiState.value.copy(isBtEnabled = status)
        }

        override fun onNetworkListUpdated(list: MutableList<ScanResult>) {
            Log.e(TAG, "onNetworkListUpdated: ")
        }

        override fun onScanStatusUpdated(status: ScanningStatus)
        {
            Log.e(TAG, "onScanStatusUpdated: This should not be called")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            _uiState.value = _uiState.value.copy(isWifiEnabled = status)
        }

        override fun onWifiAlreadyConnected(status: Boolean) {
            Log.i(TAG, "onWifiAlreadyConnected: $status")
        }
    }
    init {
        repository.registerListener(listener)
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun navigateHome(){
        if(this::navController.isInitialized){
            navController.navigate("home_first")
        }
    }

    fun onRetryPressed(navController: NavController) {
        navController.navigate(
            target = WifiNavigation.ScanningRoute,
            param = WifiNavigation.ScanningRoute.PARAM_WIFI_CONNECTION
        )
    }

}