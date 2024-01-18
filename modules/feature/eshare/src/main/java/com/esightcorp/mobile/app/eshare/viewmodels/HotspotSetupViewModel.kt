package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import android.util.Log
import androidx.navigation.NavController
import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository.HotspotCredential
import com.esightcorp.mobile.app.eshare.repositories.EshareRepositoryListener
import com.esightcorp.mobile.app.eshare.state.HotspotSetupUiState
import com.esightcorp.mobile.app.eshare.state.RadioState
import com.esightcorp.mobile.app.ui.R
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
    eshareRepository: EshareRepository,
) : EshareViewModel(application, eshareRepository), EshareRepositoryListener {
    private val _tag = this.javaClass.simpleName

    private var _uiState = MutableStateFlow(HotspotSetupUiState())
    val uiState: StateFlow<HotspotSetupUiState> = _uiState.asStateFlow()

    init {
        configureBtConnectionListener {
            _uiState.update { it.copy(isDeviceConnected = false) }
        }

        eshareRepository.setupEshareListener(this)

        when (val hpCredential = eshareRepository.genHotspotCredential()) {
            null -> {
                Log.e(_tag, "Fail generating hotspot credential. ESight connected???", Exception())
            }

            else -> {
                updateHotspotCredential(hpCredential)

                eshareRepository.startHotspotOnHMD(hpCredential)
            }
        }
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

    private fun updateHotspotCredential(hpCredential: HotspotCredential?) = _uiState.update {
        it.copy(hotspotCredential = hpCredential)
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
    //endregion
}
