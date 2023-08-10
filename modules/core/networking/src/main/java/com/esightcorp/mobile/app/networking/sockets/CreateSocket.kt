package com.esightcorp.mobile.app.networking.sockets

import android.util.Log
import java.io.Closeable
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

private const val TAG = "CreateSocket"
class CreateSocket(
    private val port:Int,
    private val createSocketListener: CreateSocketListener,
    private val inputStreamListener: InputStreamListener
): Thread(), Closeable {
    private var socket: Socket? = null
    private var inputStream: InputStream? = null
    private var serverSocket: ServerSocket? = null

    override fun run() {
        try {
            Log.i(TAG, (if (serverSocket != null) serverSocket.hashCode().toString() else "") + " <><><> starting server on port $port <><><>")
            serverSocket = ServerSocket(port)
            createSocketListener.onSocketCreated()
            Log.i(TAG, "run: <><><> waiting for client <><><>")
            socket = serverSocket!!.accept()
            Log.i(TAG, "run: <><><> accepted client <><><>")
            inputStream = socket!!.getInputStream()
            if(inputStream != null){
                inputStreamListener.onInputStreamCreated(inputStream!!)
            }else{
                inputStreamListener.onInputStreamError()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            createSocketListener.onSocketError()
        }
    }


    override fun close() {
        if(inputStream != null){
            inputStream!!.close()
            inputStreamListener.onInputStreamClosed()
        }
        if(socket != null){
            socket!!.close()
            createSocketListener.onSocketClosed()
        }
    }
}