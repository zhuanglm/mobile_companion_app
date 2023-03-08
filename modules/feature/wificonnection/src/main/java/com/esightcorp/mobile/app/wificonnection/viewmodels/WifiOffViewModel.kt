package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepoListener
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
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
): AndroidViewModel(application) {
    private val TAG = "WifiOffViewModel"
    private var _uiState = MutableStateFlow(WifiOffUiState())
    val uiState: StateFlow<WifiOffUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController
    private val listener = object : WifiConnectionRepoListener {

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

        override fun onWifiConnected(success: Boolean) {
            Log.e(TAG, "onWifiConnected: This should not be called")
        }

        override fun onWifiStatusUpdate(status: Boolean) {
            _uiState.value = _uiState.value.copy(isWifiEnabled = status)
        }
    }
    init {
        repository.registerListener(listener)
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun onBackClicked(){
        if(this::navController.isInitialized){
            navController.popBackStack()
        }
    }

    fun navigateHome(){
        if(this::navController.isInitialized){
            navController.navigate("home_first/")
        }
    }

}