package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.repositories.HotspotCredentialGenerator
import com.esightcorp.mobile.app.eshare.state.DeviceConnectionState
import com.esightcorp.mobile.app.eshare.state.HotspotSetupUiState
import com.esightcorp.mobile.app.eshare.state.RadioState
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HotspotSetupViewModel @Inject constructor(
    private val application: Application,
    private val eshareRepository: EshareRepository,
) : EshareViewModel(application), EshareRepositoryListener {

    private var _uiState = MutableStateFlow(HotspotSetupUiState())
    val uiState: StateFlow<HotspotSetupUiState> = _uiState.asStateFlow()

    init {
        updateNetworkName(HotspotCredentialGenerator.generateHotspotName())
        updateNetworkPassword(HotspotCredentialGenerator.generateHotspotPassword())
        eshareRepository.setupEshareListener(this)
        startHotspotOnHMD()
    }

    //region Navigation

    fun showHowToConnectPage() = with(application.applicationContext) {
        openExternalUrl(getString(R.string.url_esight_support))
    }

    fun showHotspotSetupErrorPage(navController: NavController) = with(navController) {
        navigate(
            target = EShareNavigation.ConnectionStoppedRoute,
            param = EShareStoppedReason.HOTSPOT_ERROR.name
        )
    }

    //endregion

    //region EshareRepositoryListener callback
    override fun onBluetoothDeviceDisconnected() {
        updateBluetoothConnectionState(false)
    }

    override fun onBluetoothDisabled() {
        updateBluetoothState(false)
    }

    override fun onWifiStateChanged(state: Boolean) {
        updateWifiState(state)
    }

    override fun onHotspotStateChanged(state: HotspotStatus?) =
        _uiState.update { it.copy(hotspotStatus = state) }
    //endregion

    //region Internal implementation

    private fun updateNetworkName(networkName: String) = _uiState.update { uiState ->
        uiState.copy(networkName = networkName)
    }

    private fun updateNetworkPassword(networkPassword: String) = _uiState.update { uiState ->
        uiState.copy(networkPassword = networkPassword)
    }

    private fun updateBluetoothState(state: Boolean) = _uiState.update { uiState ->
        uiState.copy(
            radioState = RadioState(
                isBtEnabled = state, isWifiEnabled = uiState.radioState.isWifiEnabled
            )
        )
    }

    private fun updateWifiState(state: Boolean) = _uiState.update { uiState ->
        uiState.copy(
            radioState = RadioState(
                isBtEnabled = uiState.radioState.isBtEnabled, isWifiEnabled = state
            )
        )
    }

    private fun updateBluetoothConnectionState(state: Boolean) = _uiState.update { uiState ->
        uiState.copy(isDeviceConnected = DeviceConnectionState(isDeviceConnected = state))
    }

    private fun startHotspotOnHMD() = eshareRepository.startHotspotOnHMD()
    //endregion
}
