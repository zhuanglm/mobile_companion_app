/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.WifiType
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import com.esightcorp.mobile.app.wificonnection.state.WifiTypeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WifiTypeViewModel @Inject constructor(
    application: Application,
    val repository: WifiConnectionRepository
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(WifiTypeUiState())
    val uiState: StateFlow<WifiTypeUiState> = _uiState.asStateFlow()

    fun onTypePressed(navController: NavController, type: WifiType) {
        repository.setWifiType(type)
        navController.popBackStack()
    }

    init {
        refreshUiState()
    }

    private fun refreshUiState() {
        with(repository.wifiCredentials) {
            _uiState.update {
                it.copy(wifiType = getWifiType())
            }
        }
    }
}
