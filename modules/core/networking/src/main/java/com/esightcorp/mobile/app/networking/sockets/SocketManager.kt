/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.sockets

import android.view.Surface
import com.esightcorp.mobile.app.networking.streaming.StreamOutListener
import com.esightcorp.mobile.app.networking.streaming.StreamOutThread
import java.io.InputStream

object SocketManager {
    private var createSocket: CreateSocket? = null

    fun connect(
        port: Int,
        createSocketListener: CreateSocketListener,
        inputStreamListener: InputStreamListener
    ) {
        createSocket = CreateSocket(port, createSocketListener, inputStreamListener)
        createSocket!!.start()
    }

    fun close() {
        try {
            createSocket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startStreamingFromHMD(
        surface: Surface,
        inputStream: InputStream,
        streamOutListener: StreamOutListener,
    ) {
        StreamOutThread(surface, inputStream, streamOutListener).start()
    }
}
