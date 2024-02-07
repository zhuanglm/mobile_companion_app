/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.repositories.EShareRepoManager
import com.esightcorp.mobile.app.eshare.repositories.EShareRepoManagerImpl
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.state.EshareStoppedUiState
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EshareConnectionStoppedViewModel @Inject constructor(
    application: Application,
    private val eshareRepository: EshareRepository,
) : ESightBaseViewModel(application),
    EShareRepoManager by EShareRepoManagerImpl(eshareRepository) {

    private var _uiState = MutableStateFlow(EshareStoppedUiState())
    val uiState: StateFlow<EshareStoppedUiState> = _uiState.asStateFlow()

    init {
        configureBtConnectionListener {
            _uiState.update { it.copy(isDeviceConnected = false) }
        }
    }

    fun updateState(stoppedReason: EShareStoppedReason?) = when (stoppedReason) {
        EShareStoppedReason.USER_DECLINED -> _uiState.update {
            it.copy(
                titleId = R.string.kEshareErrorViewControllerConnectionUnsuccessfulTitle,
                descriptionId = R.string.kEshareErrorViewControllerConnectionUnsuccessfulDescription,
                descriptionTwoId = R.string.kEshareErrorViewControllerTryAgainLater
            )
        }

        EShareStoppedReason.EXISTING_ACTIVE_SESSION -> _uiState.update {
            it.copy(
                titleId = R.string.kEshareErrorViewControllerConnectionUnavailableTitle,
                descriptionId = R.string.kEshareErrorActiveSessionInProgress,
                descriptionTwoId = R.string.kEshareErrorViewControllerTryAgainLater
            )
        }

        EShareStoppedReason.REMOTE_STOPPED -> _uiState.update {
            it.copy(
                titleId = R.string.kEshareErrorViewControllerConnectionStoppedTitle,
                descriptionId = R.string.kEshareErrorViewControllerConnectionStoppedDescription
            )
        }

        EShareStoppedReason.HOTSPOT_ERROR -> _uiState.update {
            it.copy(
                titleId = R.string.kEshareErrorViewControllerConnectionUnsuccessfulTitle,
                descriptionId = R.string.kHotspotUnreachable,
                descriptionTwoId = R.string.kEshareErrorViewControllerTryAgainLater
            )
        }

        else -> {}
    }
}
