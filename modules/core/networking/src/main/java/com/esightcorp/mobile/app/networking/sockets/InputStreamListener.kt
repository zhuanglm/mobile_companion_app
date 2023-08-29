package com.esightcorp.mobile.app.networking.sockets

import java.io.InputStream

interface InputStreamListener {
    fun onInputStreamCreated(inputStream: InputStream)
    fun onInputStreamClosed()
    fun onInputStreamError()
}