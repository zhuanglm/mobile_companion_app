/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.repositories


/**
 * Data layer implementation for [HomeScreen]
 */
interface HomeRepositoryListener {
    fun onBluetoothDisabled()
    fun onBluetoothEnabled()
    fun onBluetoothDeviceDisconnected()
}
