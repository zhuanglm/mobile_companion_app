package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiQrCodeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WifiQrViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
) : AndroidViewModel(application) {


    private val TAG = "WifiQrViewModel"
    private var _uiState = MutableStateFlow(WifiQrCodeUiState())
    val uiState: StateFlow<WifiQrCodeUiState> = _uiState.asStateFlow()

    fun onBackPressed(navController: NavController) {

    }

    fun onHowToScanClicked(navController: NavController) {

    }

    fun onReturnToHomeClicked(navController: NavController) {
        navController.popBackStack("home_first", false)
    }

}