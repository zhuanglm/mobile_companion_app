/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.streaming

interface StreamOutListener {
    fun onConnectionEstablished()
    fun onConnectionClosed()

    //TODO: should combine these "exiting" states inside 1 callback
    fun onConnectionError()
    fun onConnectionTimeout()
}