package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.AlreadyConnectedUiState
import com.esightcorp.mobile.app.wificonnection.state.WifiCredentialsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class AlreadyConnectedViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
):AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(AlreadyConnectedUiState())
    val uiState: StateFlow<AlreadyConnectedUiState> = _uiState.asStateFlow()

    init {

    }

}