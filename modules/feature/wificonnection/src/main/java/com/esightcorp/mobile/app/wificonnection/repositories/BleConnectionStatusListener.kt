/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection.repositories

interface BleConnectionStatusListener {
    /**
     * Callback to update BLE remote device connection status
     *
     * @param isConnected
     *   - `true`: remote BLE device connected
     *   - `false`: remote BLE device disconnected
     */
    fun onBleConnectionStatusUpdate(isConnected: Boolean) {}
}
