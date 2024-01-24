/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BtDisabledUiState
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BtDisabledViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(BtDisabledUiState())
    val uiState: StateFlow<BtDisabledUiState> = _uiState.asStateFlow()

    fun onBtEnabled(navController: NavController) = with(navController) {
        navigate(BtConnectionNavigation.IncomingRoute)
    }

    fun updateBtEnabledState(state: Boolean) = _uiState.update {
        it.copy(isBtEnabled = state)
    }
}
