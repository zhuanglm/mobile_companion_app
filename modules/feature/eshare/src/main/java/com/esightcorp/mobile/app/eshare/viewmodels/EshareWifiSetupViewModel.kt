/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EshareWifiSetupViewModel @Inject constructor(
    application: Application,
    eshareRepo: EshareRepository,
) : EshareViewModel(application, eshareRepo) {

    fun gotoWifiSetup(navController: NavController) = with(navController) {
        navigate(
            target = WifiNavigation.ScanningRoute,
            param = WifiNavigation.ScanningRoute.PARAM_BLUETOOTH,
        )
    }
}
