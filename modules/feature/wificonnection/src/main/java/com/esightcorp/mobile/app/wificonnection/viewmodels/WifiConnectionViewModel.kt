package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.wificonnection.repositories.IWifiConnectionRespository
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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


    fun updateSsid(ssid: String){
        _uiState.update { state ->
            state.copy(ssid = ssid)
        }
    }

    fun updatePassword(password:String){
        _uiState.update { state ->
            state.copy(password = password)
        }
    }

    fun updateWifiType(type:String){
        _uiState.update { state ->
            state.copy(wifiType = type)
        }
    }

    fun sendWifiCredsViaBluetooth(){
        TODO("Validate the inputs at this level")
        wifiConnectionRepository.sendWifiCreds(_uiState.value.ssid, _uiState.value.password, _uiState.value.wifiType)
    }





}