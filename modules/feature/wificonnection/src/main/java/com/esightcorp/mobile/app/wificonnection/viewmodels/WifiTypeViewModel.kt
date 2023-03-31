package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiTypeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WifiTypeViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(WifiTypeUiState())
    val uiState: StateFlow<WifiTypeUiState> = _uiState.asStateFlow()

    init {

    }



    public override fun onCleared() {
        super.onCleared();
    }

    private fun onWifiTypeSelected(wifiType: String) {
        _uiState.update {
            it.copy(
                wifiType = wifiType
            )
        }
    }

    fun onTypePressed(navController: NavController, type: String){
        repository.setWifiType(type)
        navController.popBackStack()
    }








}