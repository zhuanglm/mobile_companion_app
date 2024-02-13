/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.bluetooth

import android.bluetooth.BluetoothGattCharacteristic

data class BleGattOperation(val data: ByteArray, val characteristic: BluetoothGattCharacteristic){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BleGattOperation

        if (!data.contentEquals(other.data)) return false
        return characteristic == other.characteristic
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + characteristic.hashCode()
        return result
    }

}
