/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection.viewmodels

import android.app.Application
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import com.esightcorp.mobile.app.btconnection.repositories.BtHwStateManagerImpl
import com.esightcorp.mobile.app.btconnection.repositories.IBtHwStateManager
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation
import com.esightcorp.mobile.app.utils.permission.IPermissionManager
import com.esightcorp.mobile.app.utils.permission.PermissionManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoDevicesConnectedViewModel @Inject constructor(
    application: Application,
    private val btRepo: BtConnectionRepository,
) : ESightBaseViewModel(application),
    IPermissionManager by PermissionManagerImpl(application, btRepo.bluetoothPermissions),
    IBtHwStateManager by BtHwStateManagerImpl(btRepo) {

    //region Navigation

    fun navigateToBtDisabled(nav: NavController) = with(nav) {
        navigate(BtConnectionNavigation.BtDisabledScreen)
    }

    fun navigateToBtScanning(nav: NavController) = with(nav) {
        navigate(BtConnectionNavigation.BtSearchingRoute)
    }

    fun navigateToBtPermission(nav: NavController) = with(nav) {
        navigate(target = BtConnectionNavigation.BtPermissionRoute, popCurrent = true)
    }

    fun navigateToSettings(nav: NavController) = nav.navigate(SettingsNavigation.IncomingRoute)

    //endregion

}
