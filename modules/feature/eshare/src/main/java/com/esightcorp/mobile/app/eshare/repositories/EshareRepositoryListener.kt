/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.repositories

import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import java.io.InputStream

interface EshareRepositoryListener {
    fun onEshareStateChanged(state: EShareConnectionStatus) {}
    fun onEshareStateRequested(state: EShareConnectionStatus) {}
    fun onBluetoothDisabled() {}
    fun onWifiStateChanged(state: Boolean) {}
    fun onInputStreamCreated(inputStream: InputStream) {}

    fun onHotspotStateChanged(state: HotspotStatus?) {}
}