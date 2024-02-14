/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.utils.permission.IPermissionManager
import com.esightcorp.mobile.app.utils.permission.PermissionManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationPermissionViewModel @Inject constructor(
    application: Application,
    homeRepo: HomeRepository,
) : AndroidViewModel(application),
    IPermissionManager by PermissionManagerImpl(application, homeRepo.locationPermissions) {

    fun onDismiss(navController: NavController) = navController.popBackStack()

}
