/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BluetoothConnectionRepositoryCallback
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.state.UnableToConnectUiState
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UnableToConnectViewModel @Inject constructor(
    private val application: Application,
    btConnectionRepository: BtConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(UnableToConnectUiState())
    val uiState: StateFlow<UnableToConnectUiState> = _uiState.asStateFlow()
    private lateinit var navController: NavController

    private val listener = object : BluetoothConnectionRepositoryCallback {
        override fun onBtStateUpdate(enabled: Boolean) {
            updateBtEnabledState(enabled)
        }
    }

    init {
        btConnectionRepository.registerListener(listener)
        btConnectionRepository.setupBtModelListener()
    }

    private fun updateBtEnabledState(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(isBtEnabled = enabled)
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun navigateToNoDevicesConnectedScreen() {
        if (this::navController.isInitialized) {
            navController.navigate(BtConnectionNavigation.NoDeviceConnectedRoute)
        }
    }

    fun navigateToBtSearchingScreen() {
        if (this::navController.isInitialized) {
            navController.navigate(BtConnectionNavigation.BtSearchingRoute)
        }
    }

    fun showHowToConnectPage() = with(application.applicationContext) {
        openExternalUrl(getString(com.esightcorp.mobile.app.ui.R.string.url_esight_support))
    }
}
