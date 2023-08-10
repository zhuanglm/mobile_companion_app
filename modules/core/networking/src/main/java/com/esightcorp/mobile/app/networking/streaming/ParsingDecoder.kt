package com.esightcorp.mobile.app.networking.streaming

import android.util.Log
import java.io.Closeable
import java.io.IOException


/**
 * A class responsible for consuming byte data and interacting with a decoder.
 *
 * @property mBuffer A buffer that temporarily holds incoming byte data up to a specified BUFFER_SIZE.
 * @property mLocation The current position within the mBuffer to which new byte data should be written.
 * @property mValue Represents a 4-byte value from the stream. When its value is 0x00000001, it gets passed to the mDecoder.
 */
class ParsingDecoder(
    private val decoder: Decoder
) : Closeable {

    private var mBuffer = ByteArray(BUFFER_SIZE)
    private var mLocation = 0
    private var mValue =
        0xFFFFFFFF // 4 bytes in the stream.  When it is 0x00000001 we pass it to the mDecoder

    /**
     * Consumes the given byte data and updates the buffer and associated properties.
     *
     * The function iterates through the given byte data and adds it to the buffer. If the buffer overflows, an exception is thrown.
     * If the mValue becomes 0x00000001, the current buffer data is passed to the decoder for further processing.
     *
     * @param data The byte data to be consumed.
     * @throws IOException if the buffer overflows beyond BUFFER_SIZE.
     */
    fun decode(data: ByteArray) {
        Log.i(TAG, "decode: data size:  ${data.size} ")
        if (data.isEmpty()) return
        for ((index, byte) in data.withIndex()) {
            if (mLocation >= BUFFER_SIZE) {
                throw IOException("Buffer overflow")
            }
            mBuffer[mLocation] = byte
            mValue = (mValue shl 8) or (byte.toLong() and 0x000000FF)
            if (mValue == 0x00000001.toLong()) {
                decoder.readFrame(
                    mBuffer, mLocation + 1
                ) // +1 because we want to include the 0x00000001
                mLocation = 0
                mValue = 0xFFFFFFFF
            } else {
                mLocation++
            }
        }
        return
    }

    /**
     * Closes the associated decoder, releasing any resources it might be holding.
     */
    override fun close() {
        decoder.close()
    }

    companion object {
        private const val TAG = "ParsingDecoder"
        private const val BUFFER_SIZE = 1920 * 1080

    }
}