/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.sockets

import android.util.Log
import java.io.Closeable
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

class CreateSocket(
    private val port: Int,
    private val createSocketListener: CreateSocketListener,
    private val inputStreamListener: InputStreamListener
) : Thread(), Closeable {
    private val _tag = this.javaClass.simpleName

    private var socket: Socket? = null
    private var inputStream: InputStream? = null
    private var serverSocket: ServerSocket? = null

    override fun run() {
        try {
            serverSocket = ServerSocket(port)

            Log.i(
                _tag,
                "<><><> starting server on port $port (socket: ${serverSocket.hashCode()}) <><><>"
            )

            createSocketListener.onSocketCreated()
            Log.i(_tag, "run: <><><> waiting for client <><><>")
            socket = serverSocket!!.accept()
            Log.i(_tag, "run: <><><> accepted client <><><>")

            socket?.getInputStream()?.let {
                inputStream = it
                inputStreamListener.onInputStreamCreated(it)
            } ?: run {
                inputStreamListener.onInputStreamError()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            createSocketListener.onSocketError()
        }
    }


    override fun close() {
        inputStream?.let {
            it.close()
            inputStreamListener.onInputStreamClosed()
        }

        socket?.let {
            it.close()
            createSocketListener.onSocketClosed()
        }
    }
}
