package com.esightcorp.mobile.app.networking.streaming

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import java.io.InputStream

class StreamOutThread(
    private val surface: Surface,
    private val inputStream: InputStream,
    private val streamOutListener: StreamOutListener
) : Thread() {

    private var isReadingData = false;
    private val mHandler = Handler(Looper.getMainLooper())
    private val timeoutRunnable = Runnable {
        if (isReadingData) {
            isReadingData = false
            restartTimeout()
        } else {
            streamOutListener.onConnectionTimeout()
        }
    }

    private fun restartTimeout() {
        mHandler.removeCallbacks(timeoutRunnable)
        mHandler.postDelayed(timeoutRunnable, TIMEOUT)
    }

    override fun run() {
        restartTimeout()
        try {
            val decoder = ParsingDecoder(DecoderImpl(surface))
            val buffer = ByteArray(BUFFER_SIZE)
            while (!isInterrupted) {
                Log.d(TAG, "run: reading data")
                isReadingData = true
                val read = inputStream.read(buffer)
                if (read == -1 || read == 0) {
                    Log.d(TAG, "run: read -1 or 0  ")
                    break
                }
                Log.d(TAG, "run: read $read bytes")
                decoder.consume(buffer.copyOf(read))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            streamOutListener.onConnectionError()
        } finally {
            mHandler.removeCallbacks(timeoutRunnable)
        }
    }

    companion object {
        private const val TAG = "StreamOutThread"
        private const val TIMEOUT = 7000L
        private const val BUFFER_SIZE = 1024 * 2
    }
}