package com.esightcorp.mobile.app.networking.streaming

interface StreamOutListener {
    fun onConnectionEstablished()
    fun onConnectionClosed()
    fun onConnectionError()
    fun onConnectionTimeout()
}