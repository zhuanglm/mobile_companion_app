/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

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
