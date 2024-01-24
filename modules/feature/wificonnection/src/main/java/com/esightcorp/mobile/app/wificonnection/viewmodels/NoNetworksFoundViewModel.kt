/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.networking.storage.WifiCache
import com.esightcorp.mobile.app.ui.components.openExternalUrl
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoNetworksFoundViewModel @Inject constructor(
    private val application: Application,
    private val wifiRepo: WifiConnectionRepository,
) : ESightBaseViewModel(application) {

    fun showHowToConnectPage() = with(application.applicationContext) {
        openExternalUrl(getString(com.esightcorp.mobile.app.ui.R.string.url_esight_support))
    }

    fun tryAgain(navController: NavController) {
        navController.navigate(
            target = WifiNavigation.ScanningRoute,
            param = when (wifiRepo.wifiFlow) {
                WifiCache.WifiFlow.QrFlow -> WifiNavigation.ScanningRoute.PARAM_QR
                else -> WifiNavigation.ScanningRoute.PARAM_BLUETOOTH
            }
        )
    }
}
