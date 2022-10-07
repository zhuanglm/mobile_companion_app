package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.home.state.HomeUiEvent
import com.esightcorp.mobile.app.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    homeRepository: HomeRepository
): AndroidViewModel(application) {

    private var _uiState = mutableStateOf(HomeUiState())
    val uiState: State<HomeUiState> = _uiState


    fun onEvent(event: HomeUiEvent){
        when (event){
            is HomeUiEvent.BluetoothConnected -> {
                _uiState.value.copy(
                    isBluetoothConnected = event.isConnected
                )
            }
            is HomeUiEvent.BluetoothEnabled -> {
                _uiState.value.copy(
                    isBluetoothEnabled = event.isEnabled
                )
            }
            is HomeUiEvent.PermissionsGranted -> {
                _uiState.value.copy(
                    arePermissionsGranted = event.areGranted
                )
            }
        }
    }


}