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
