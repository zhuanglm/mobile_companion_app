package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import android.util.Log
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.repositories.HotspotCredentialGenerator
import com.esightcorp.mobile.app.eshare.state.DeviceConnectionState
import com.esightcorp.mobile.app.eshare.state.HotspotSetupUiState
import com.esightcorp.mobile.app.eshare.state.RadioState
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class HotspotSetupViewModel @Inject constructor(
    private val eshareRepository: EshareRepository, application: Application
) : ESightBaseViewModel(application), EshareRepositoryListener {

    private val _tag = this.javaClass.simpleName

    override fun onEshareStateChanged(state: EShareConnectionStatus) {
        Log.i(_tag, "onEshareStateChanged: Should not happen here")
    }

    override fun onEshareStateRequested(state: EShareConnectionStatus) {
        TODO("Not yet implemented")
    }

    override fun onBluetoothDeviceDisconnected() {
        updateBluetoothConnectionState(false)
    }

    override fun onBluetoothDisabled() {
        updateBluetoothState(false)
    }

    override fun onWifiStateChanged(state: Boolean) {
        updateWifiState(state)
    }

    override fun onInputStreamCreated(inputStream: InputStream) {
        Log.i(_tag, "onInputStreamCreated: Should not happen here")
    }

    private var _uiState = MutableStateFlow(HotspotSetupUiState())
    val uiState: StateFlow<HotspotSetupUiState> = _uiState.asStateFlow()

    private fun updateNetworkName(networkName: String) {
        _uiState.update { uiState ->
            uiState.copy(networkName = networkName)
        }
    }

    private fun updateNetworkPassword(networkPassword: String) {
        _uiState.update { uiState ->
            uiState.copy(networkPassword = networkPassword)
        }
    }

    private fun updateBluetoothState(state: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(
                radioState = RadioState(
                    isBtEnabled = state, isWifiEnabled = uiState.radioState.isWifiEnabled
                )
            )
        }
    }

    private fun updateWifiState(state: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(
                radioState = RadioState(
                    isBtEnabled = uiState.radioState.isBtEnabled, isWifiEnabled = state
                )
            )
        }
    }

    private fun updateBluetoothConnectionState(state: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(isDeviceConnected = DeviceConnectionState(isDeviceConnected = state))
        }
    }


    init {
        updateNetworkName(HotspotCredentialGenerator.generateHotspotName())
        updateNetworkPassword(HotspotCredentialGenerator.generateHotspotPassword())
        eshareRepository.setupEshareListener(this)
        startHotspotOnHMD()
    }

    private fun startHotspotOnHMD() {
        eshareRepository.startHotspotOnHMD()
    }
}
