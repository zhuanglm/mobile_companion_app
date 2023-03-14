package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.UnableToConnectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class UnableToConnectViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
): AndroidViewModel(application) {
    private var _uiState = MutableStateFlow(UnableToConnectUiState())
    val uiState: StateFlow<UnableToConnectUiState> = _uiState.asStateFlow()
}