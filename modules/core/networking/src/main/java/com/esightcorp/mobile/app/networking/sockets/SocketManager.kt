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
