package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.home.repositories.IHomeRepository
import com.esightcorp.mobile.app.home.state.HomeUiEvent
import com.esightcorp.mobile.app.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
private const val TAG = "HomeViewModel"
@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    homeRepository: HomeRepository
): AndroidViewModel(application) {

    /**
     * Object which is used by the compose UI to track UI State
     */
    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateConnectedDevice(device: String){
        Log.d(TAG, "updateConnectedDevice: ${device}")
        _uiState.update { currentState ->
            currentState.copy(connectedDevice = device, isBluetoothConnected = true)
        }
    }

    fun navigateToWifiCredsOverBt(navController: NavController){
        navController.navigate("wificonnection_home")
    }

}