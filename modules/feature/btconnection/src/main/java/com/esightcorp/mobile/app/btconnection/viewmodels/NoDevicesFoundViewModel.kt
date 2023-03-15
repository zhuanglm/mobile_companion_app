package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.NoDevicesFoundUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NoDevicesFoundViewModel @Inject constructor(
    application: Application, val btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(NoDevicesFoundUiState())
    val uiState: StateFlow<NoDevicesFoundUiState> = _uiState.asStateFlow()

}