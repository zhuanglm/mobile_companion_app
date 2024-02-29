/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UnableToConnectViewModel @Inject constructor(
    application: Application,
    private val wifiRepo: WifiConnectionRepository,
) : ESightBaseViewModel(application) {

    fun onTryAgain(navController: NavController) {
        with(wifiRepo) {
            if (!wifiCredentials.isValid()) return

            // Resend wifi credential
            wifiRepo.sendWifiCreds(
                wifiCredentials.getPassword(),
                wifiCredentials.getWifiType().typeString
            )

            // Then navigate to Wifi-Connecting
            navController.navigate(target = WifiNavigation.ConnectingRoute)
        }
    }

    fun onBackPressed(navController: NavController) = navController.popBackStack()
}
