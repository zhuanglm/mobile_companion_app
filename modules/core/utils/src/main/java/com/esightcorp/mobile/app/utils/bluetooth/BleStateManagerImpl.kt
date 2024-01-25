/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils.bluetooth

import android.util.Log
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.GoProduct

/**
 * Delegate implementation
 */
class BleStateManagerImpl : IBleStateManager {
    private val _tag = this.javaClass.simpleName

    override fun onBleDisconnected(navController: NavController) {
        Log.e(_tag, "--> onBleDisconnected - Going to Disconnection screen ...")
        navController.navigate(
            target = BtConnectionNavigation.DeviceDisconnectedRoute,
            popUntil = GoProduct.IncomingRoute,
        )
    }
}
