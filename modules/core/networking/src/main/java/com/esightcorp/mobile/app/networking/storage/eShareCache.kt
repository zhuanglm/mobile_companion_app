/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.storage

object eShareCache {

    private var port: Int = -1
    private var ipAddress: String = ""

    fun setPort(port: Int) {
        this.port = port
    }

    fun setIpAddress(ipAddress: String) {
        this.ipAddress = ipAddress
    }

    fun getPort(): Int {
        return port
    }

    fun getIpAddress(): String {
        return ipAddress
    }

    fun clear() {
        port = -1
        ipAddress = ""
    }
}