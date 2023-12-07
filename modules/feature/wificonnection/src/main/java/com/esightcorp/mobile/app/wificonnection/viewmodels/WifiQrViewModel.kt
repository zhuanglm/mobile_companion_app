package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiQrCodeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WifiQrViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
) : AndroidViewModel(application) {

    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(WifiQrCodeUiState())
    val uiState: StateFlow<WifiQrCodeUiState> = _uiState.asStateFlow()

    init {
        setQrString(repository.qrString)
    }

    fun onBackPressed(navController: NavController) {
        navController.popBackStack()
    }

    fun onHowToScanClicked(navController: NavController) {
        navController.navigate(WifiConnectionScreens.HowToScanQrRoute.route)
    }

    fun onReturnToHomeClicked(navController: NavController) {
        navController.popBackStack("home_first", false)
    }

    private fun setQrString(qrString: String?) {
        when (qrString) {
            null -> Log.e(_tag, "QrString is null! Wifi was not selected properly!", Exception())

            else -> {
                Log.i(_tag, "setQrString: $qrString")
                _uiState.update { it.copy(qrString = qrString) }
            }
        }
    }
}
