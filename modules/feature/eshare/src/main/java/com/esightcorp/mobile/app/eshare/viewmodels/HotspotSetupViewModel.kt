package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.repositories.HotspotCredentialGenerator
import com.esightcorp.mobile.app.eshare.state.EshareConnectingUiState
import com.esightcorp.mobile.app.eshare.state.HotspotSetupUiState
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class HotspotSetupViewModel @Inject constructor(
    val eshareRepository: EshareRepository,
    application: Application
): AndroidViewModel(application), EshareRepositoryListener {

    override fun onEshareStateChanged(state: eShareConnectionStatus) {
        TODO("Not yet implemented")
    }

    override fun onEshareStateRequested(state: eShareConnectionStatus) {
        TODO("Not yet implemented")
    }

    override fun onBluetoothStateChanged(state: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onWifiStateChanged(state: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onInputStreamCreated(inputStream: InputStream) {
        TODO("Not yet implemented")
    }

    private var _uiState = MutableStateFlow(HotspotSetupUiState())
    val uiState: StateFlow<HotspotSetupUiState> = _uiState.asStateFlow()

    private fun updateNetworkName(networkName: String){
        _uiState.update { uiState ->
            uiState.copy(networkName = networkName)
        }
    }

    private fun updateNetworkPassword(networkPassword: String){
        _uiState.update { uiState ->
            uiState.copy(networkPassword = networkPassword)
        }
    }

    init {
        updateNetworkName(HotspotCredentialGenerator.generateHotspotName())
        updateNetworkPassword(HotspotCredentialGenerator.generateHotspotPassword())
        eshareRepository.setupEshareListener(this)
        startHotspotOnHMD()
    }

    private fun startHotspotOnHMD(){
        eshareRepository.startHotspotOnHMD()
    }




}

