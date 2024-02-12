/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.utils.permission.IPermissionManager
import com.esightcorp.mobile.app.utils.permission.PermissionManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BtPermissionViewModel @Inject constructor(
    application: Application,
    private val btRepo: BtConnectionRepository,
) : AndroidViewModel(application),
    IPermissionManager by PermissionManagerImpl(application, btRepo.bluetoothPermissions) {

    fun onPermissionGranted(navController: NavController) = navController.popBackStack()
}
