/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking

import com.esightcorp.mobile.app.ui.R

enum class WifiType(val stringValueResId: Int, val typeString: String) {
    WPA(R.string.kWifiSecurityTypeWPA, "WPA/WPA2/WPA3"),
    WEP(R.string.kWifiSecurityTypeWEP,"WEP"),
    NONE(R.string.kWifiSecurityTypeNone, "nopass");

    companion object {
        fun fromString(value: String): WifiType? {
            return values().find { it.typeString.equals(value, ignoreCase = true) }
        }
    }
}