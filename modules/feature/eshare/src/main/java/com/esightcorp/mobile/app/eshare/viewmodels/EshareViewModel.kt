/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.repositories.EShareRepoManager
import com.esightcorp.mobile.app.eshare.repositories.EShareRepoManagerImpl
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
open class EshareViewModel @Inject constructor(
    application: Application,
    private val eshareRepo: EshareRepository,
) : ESightBaseViewModel(application), EShareRepoManager by EShareRepoManagerImpl(eshareRepo) {

    private val _isDeviceConnected = MutableStateFlow(true)
    val devConnectionState: StateFlow<Boolean> = _isDeviceConnected.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        configureBtConnectionListener {
            _isDeviceConnected.update { false }
        }
    }

    fun onSetupHotspotPressed(navController: NavController) =
        navController.navigate(EShareNavigation.HotspotSetupRoute)

    fun onRetryPressed(navController: NavController) =
        navController.navigate(EShareNavigation.IncomingRoute)

}
