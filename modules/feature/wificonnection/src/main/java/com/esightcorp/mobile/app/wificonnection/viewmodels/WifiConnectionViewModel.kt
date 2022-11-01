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
import javax.inject.Inject


private const val TAG  = "WifiConnectionViewModel"

@HiltViewModel
class WifiConnectionViewModel @Inject constructor(
    application: Application,
    wifiConnectionRepository: WifiConnectionRepository
): AndroidViewModel(application) {


    /**
     * Holds ui states -> see WifiConnectionUiState for the full list of states and what they are
     */
    private var _uiState = MutableStateFlow(WifiConnectionUiState())
    val uiState: StateFlow<WifiConnectionUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Init call ")
    }



}