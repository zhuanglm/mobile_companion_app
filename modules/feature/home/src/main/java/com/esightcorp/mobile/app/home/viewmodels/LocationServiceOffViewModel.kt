/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.utils.permission.ILocationServiceManager
import com.esightcorp.mobile.app.utils.permission.LocationServiceManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationServiceOffViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application),
    ILocationServiceManager by LocationServiceManagerImpl(application) {

    fun gotoSystemLocationSettings(launcherInf: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        launcherInf.launch(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        )
    }

    fun onDismiss(navController: NavController) = navController.popBackStack()
}
