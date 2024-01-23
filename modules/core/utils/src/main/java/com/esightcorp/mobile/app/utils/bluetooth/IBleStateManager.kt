package com.esightcorp.mobile.app.utils.bluetooth

import androidx.navigation.NavController

/**
 * Interface to notify the BLE state change
 */
interface IBleStateManager {
    /**
     * Notify when remote device gets disconnected
     */
    fun onBleDisconnected(navController: NavController)
}
