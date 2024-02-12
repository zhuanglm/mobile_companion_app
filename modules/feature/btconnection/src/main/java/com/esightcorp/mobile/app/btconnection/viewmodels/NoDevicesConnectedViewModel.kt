/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.BluetoothUiState
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NoDevicesConnectedViewModel @Inject constructor(
    private val application: Application,
    btConnectionRepository: BtConnectionRepository,
) : ESightBaseViewModel(application) {
    private val _tag = this.javaClass.simpleName

    /**
     * Object which is used by the compose UI to track UI State
     */
    private var _uiState = MutableStateFlow(BluetoothUiState())
    val uiState: StateFlow<BluetoothUiState> = _uiState.asStateFlow()

    /**
     * Interface to receive callbacks from the bluetooth repository
     */
    private val btRepositoryListener = object : BluetoothConnectionRepositoryCallback {
        @SuppressLint("MissingPermission")
        override fun onDeviceConnected(device: BluetoothDevice?, connected: Boolean?) {
            updateBtConnectedState(connected, device?.name)
        }

        override fun onBtStateUpdate(enabled: Boolean) {
            Log.d(_tag, "onBtStateUpdate: $enabled")
            updateBtEnabledState(enabled)
        }
    }

    /**
     * First constructor is init{}
     */
    init {
        Log.d(_tag, ": INIT NoDevicesConnectedViewModel")
        btConnectionRepository.registerListener(btRepositoryListener)
        btConnectionRepository.setupBtModelListener()
    }

    //region Private implementation

    /**
     * Methods to interact with the UI state StateFlow object
     */

    private fun updateBtEnabledState(state: Boolean) = _uiState.update { currentState ->
        currentState.copy(isBtEnabled = state)
    }

    private fun updateBtConnectedState(state: Boolean?, device: String?) =
        _uiState.update { currentState ->
            val connStatus = (state == true)
            when (device) {
                null -> currentState.copy(btConnectionStatus = connStatus)
                else -> currentState.copy(btConnectionStatus = connStatus, connectedDevice = device)
            }
        }

    //endregion

    //region Navigation

    fun showFeedbackPage() = with(application.applicationContext) {
        openExternalUrl(getString(R.string.url_esight_feedback))
    }

    fun navigateToScanESight(nav: NavController) = with(nav) {
        navigate(BtConnectionNavigation.BtSearchingRoute)
    }

    fun navigateToSettings(nav: NavController) = nav.navigate(SettingsNavigation.IncomingRoute)


    fun showExternalUrl(urlId: Int) =
        with(application.applicationContext) { openExternalUrl(getString(urlId)) }

    fun openTerms(){
        showExternalUrl(R.string.url_esight_privacy_policy)
    }

    fun openPrivacyPolicy(){
        showExternalUrl(R.string.url_esight_privacy_policy)
    }

    //endregion
}
