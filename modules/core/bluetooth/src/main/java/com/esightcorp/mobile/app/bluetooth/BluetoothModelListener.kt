/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.le.ScanResult

interface BluetoothModelListener : BluetoothRadioListener, BluetoothConnectionListener {
    fun listOfDevicesUpdated() {}
    fun onBatchScanResults(results: List<ScanResult>) {}
    fun onScanFailed(error: Int) {}
    fun onScanStarted() {}
    fun onScanFinished() {}
    fun onScanCancelled() {}
}
