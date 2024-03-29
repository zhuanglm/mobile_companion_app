/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.settings.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.networking.WifiModel
import com.esightcorp.mobile.app.settings.repositories.BtConnectionManagerImpl
import com.esightcorp.mobile.app.settings.repositories.IBtConnectionManager
import com.esightcorp.mobile.app.settings.repositories.SettingsRepository
import com.esightcorp.mobile.app.settings.state.SettingsUiState
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import com.esightcorp.mobile.app.utils.bluetooth.BleStateManagerImpl
import com.esightcorp.mobile.app.utils.bluetooth.IBleStateManager
import com.esightcorp.mobile.app.utils.permission.ILocationServiceManager
import com.esightcorp.mobile.app.utils.permission.IPermissionManager
import com.esightcorp.mobile.app.utils.permission.LocationServiceManagerImpl
import com.esightcorp.mobile.app.utils.permission.PermissionManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    settingsRepo: SettingsRepository,
    btConnRepo: BtConnectionRepository,
) : AndroidViewModel(application),

    IBleStateManager by BleStateManagerImpl(),
    IBtConnectionManager by BtConnectionManagerImpl(btConnRepo),
    IPermissionManager by PermissionManagerImpl(application, WifiModel.locationPermissions),
    ILocationServiceManager by LocationServiceManagerImpl(application) {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    init {
        _settingsUiState.update { it.copy(appVersion = settingsRepo.getApplicationVersion()) }

        configureConnectionListener { connState -> _settingsUiState.update { it.copy(connState = connState) } }
    }

    fun navigateToDisconnectConfirmation(navController: NavController) =
        navController.navigate(SettingsNavigation.DisconnectConfirmationRoute)

    fun showExternalUrl(urlId: Int) =
        with(application.applicationContext) { openExternalUrl(getString(urlId)) }

    fun navigateToLocationServiceOff(navController: NavController) = with(navController) {
        navigate(target = HomeNavigation.LocationServiceOffRoute, popCurrent = false)
    }
}
