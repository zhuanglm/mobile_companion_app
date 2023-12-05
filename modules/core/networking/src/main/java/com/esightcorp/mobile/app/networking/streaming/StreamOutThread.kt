package com.esightcorp.mobile.app.networking.streaming

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import com.esightcorp.mobile.app.networking.sockets.SocketManager
import java.io.InputStream

class StreamOutThread(
    private val surface: Surface,
    private val inputStream: InputStream,
    private val streamOutListener: StreamOutListener
) : Thread() {

    private val _tag = this.javaClass.simpleName

    private var isReadingData = false
        @Synchronized get
        @Synchronized set

    private val mHandler = Handler(Looper.getMainLooper())

    private val timeoutRunnable = Runnable {
        if (isReadingData) {
            isReadingData = false
            restartTimeout()
        } else {
            Log.e(_tag, "Worker thread read timeout!", Exception())
            this@StreamOutThread.interrupt()

            try {
                inputStream.close()
            } catch (_: Throwable) {
            }

            streamOutListener.onConnectionTimeout()
        }
    }

    @Synchronized
    private fun restartTimeout() {
        mHandler.removeCallbacks(timeoutRunnable)
        mHandler.postDelayed(timeoutRunnable, TIMEOUT)
    }

    override fun run() {
        Log.e(_tag, "Worker thread started ...")

        restartTimeout()
        try {
            val decoder = ParsingDecoder(DecoderImpl(surface))
            val buffer = ByteArray(BUFFER_SIZE)
            while (!isInterrupted) {
                isReadingData = true
                val read = inputStream.read(buffer)
                if (read == -1 || read == 0) {
                    Log.e(_tag, "Input stream read result: $read")
                    streamOutListener.onConnectionClosed()
                    SocketManager.close()
                    break
                }
//                Log.d(_tag, "Received bytes: $read")
                decoder.consume(buffer.copyOf(read))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            streamOutListener.onConnectionError()
        } finally {
            mHandler.removeCallbacks(timeoutRunnable)
        }

        Log.e(_tag, "Worker thread stopped!!!")
    }

    companion object {
        private const val TIMEOUT = 10000L
        private const val BUFFER_SIZE = 1024 * 2
    }
}