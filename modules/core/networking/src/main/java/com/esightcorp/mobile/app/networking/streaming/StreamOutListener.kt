package com.esightcorp.mobile.app.networking.streaming

interface StreamOutListener {
    fun onConnectionEstablished()
    fun onConnectionClosed()

    //TODO: should combine these "exiting" states inside 1 callback
    fun onConnectionError()
    fun onConnectionTimeout()
}