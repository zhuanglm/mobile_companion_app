package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.EshareConnectingUiState
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EshareConnectingViewModel @Inject constructor(
    application: Application,
    private val eShareRepository: EshareRepository

): AndroidViewModel(application), EshareRepositoryListener{

    private val TAG = "EshareConnectingViewMod"

    private var _uiState = MutableStateFlow(EshareConnectingUiState())
    val uiState: StateFlow<EshareConnectingUiState> = _uiState.asStateFlow()

    override fun onEshareStateChanged(state: eShareConnectionStatus) {
        updateConnectionState(state)
    }

    override fun onEshareStateRequested(state: eShareConnectionStatus) {
        TODO("Not yet implemented")
    }

    override fun onBluetoothDeviceDisconnected() {
        TODO("Not yet implemented")
    }

    override fun onBluetoothDisabled() {
        TODO("Not yet implemented")
    }

    override fun onWifiStateChanged(state: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onInputStreamCreated(inputStream: InputStream) {
        TODO("Not yet implemented")
    }

    init {
        eShareRepository.setupEshareListener(this)
    }

    fun startEshareConnection(){
        eShareRepository.startEshareConnection()
    }

    private fun updateConnectionState(state: eShareConnectionStatus){
        _uiState.update { uiState ->
            uiState.copy(connectionState = state)
        }
    }

    fun onCancelClicked(){
        eShareRepository.cancelEshareConnection()
    }

    override fun onCleared() {
        super.onCleared()
    }


}