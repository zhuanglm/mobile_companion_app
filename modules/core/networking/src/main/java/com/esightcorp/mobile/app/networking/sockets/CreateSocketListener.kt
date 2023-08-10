package com.esightcorp.mobile.app.networking.sockets

interface CreateSocketListener {
    fun onSocketCreated()
    fun onSocketClosed()
    fun onSocketError()
}