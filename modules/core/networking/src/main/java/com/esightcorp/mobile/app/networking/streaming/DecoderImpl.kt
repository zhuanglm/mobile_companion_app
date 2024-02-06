/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.streaming

import android.media.MediaCodec
import android.media.MediaCodec.CodecException
import android.media.MediaCodecList
import android.media.MediaFormat
import android.util.Log
import android.view.Surface
import java.io.IOException
import java.nio.ByteBuffer

class DecoderImpl(
    surface: Surface?
) : Decoder(surface) {

    override var mDecoder: MediaCodec? = null
    override var isConfigured: Boolean = false
    override lateinit var mMediaInfo: MediaCodec.BufferInfo

    override fun initializeDecoder() {
        mSurface?.let {
            try {
                Log.w(TAG, "Surface size: ($mWidth, $mHeight), $mMime")
                val mediaFormat = MediaFormat.createVideoFormat(mMime, mWidth, mHeight)
                mDecoder = getDecoder(mediaFormat)
                mMediaInfo = MediaCodec.BufferInfo()
                mDecoder?.let { mDecoder ->
                    Log.i(TAG, "initializeDecoder: is surface valid " + mSurface.isValid)
                    mDecoder.configure(mediaFormat, mSurface, null, 0)
                    isConfigured = true
                    mDecoder.start()
                }
                Log.w(TAG, "->> initializeDecoder - done")
            } catch (e: Exception) {
                Log.e(TAG, "Initialize failed!", e)
                if (e is CodecException) {
                    Log.w(TAG, "diagnosticInfo: ${e.diagnosticInfo}")
                }

                close()
                e.printStackTrace()
                throw IOException("Decoder Start Exception")
            }
        }
    }


    @Synchronized
    override fun close() {
        Log.i(TAG, "close: Stopping decoder")
        try {
            mDecoder?.stop()
            mDecoder?.release()
            mDecoder = null
        } catch (e: Exception) {
            Log.e(TAG, "close: Exception stopping decoder", e)
        }
    }


    /**
     * Processes and decodes a given frame of data using the initialized decoder.
     *
     * This method reads the provided data input, queues it for decoding, and then
     * checks for output from the decoder. If there's a decoded output available,
     * it will be rendered to the output surface. Additionally, if there's a change
     * in the output format, the method will notify any registered listeners and
     * update the internal state accordingly.
     *
     * @param dataInput The byte array containing the data to be decoded.
     * @param length The length of the valid data in the byte array.
     *
     * @throws Exception if there's an error during the decoding process. This
     * exception is caught internally and logged, but not propagated further.
     */
    @Synchronized
    override fun readFrame(dataInput: ByteArray, length: Int) {
//        Log.i(TAG, "readFrame: Trying to read frame")
        if (mDecoder == null) {
            Log.e(TAG, "readFrame: Decoder is null")
            return
        }
        val inputIndex = mDecoder!!.dequeueInputBuffer(TIMEOUT)
//        Log.d(TAG, "readFrame: input index $inputIndex")
        if (inputIndex >= 0) {
            val buffer: ByteBuffer? = mDecoder!!.getInputBuffer(inputIndex)
            if (buffer != null) {
//                Log.i(TAG, "readFrame: buffer is not null")
                buffer.clear()
                buffer.put(mAccessUnit)
                buffer.put(dataInput, 0, length)
                mDecoder!!.queueInputBuffer(inputIndex, 0, length, 0, 0)
            }
        }
        try {
            val outputIndex = mDecoder!!.dequeueOutputBuffer(MediaCodec.BufferInfo(), TIMEOUT)
//            Log.d(TAG, "readFrame: output index $outputIndex")
//            Log.d(TAG, "readFrame: isConfigured $isConfigured")
            if (outputIndex >= 0 && isConfigured) {
//                Log.i(TAG, "readFrame: release output buffer called")
                mDecoder!!.releaseOutputBuffer(outputIndex, true)
            } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                val format = mDecoder!!.outputFormat
//                Log.i(TAG, "readFrame: Size changed")
                onSizeChanged?.onSizeChanged(
                    format.getInteger(MediaFormat.KEY_WIDTH),
                    format.getInteger(MediaFormat.KEY_HEIGHT)
                )
                isConfigured = true
//                Log.i(TAG, "readFrame: Output format changed")
            }
        } catch (e: Exception) {
            Log.e(TAG, "readFrame: Exception reading frame", e)
        }
    }


    /**
     * Retrieves a [MediaCodec] instance suitable for decoding the provided media format.
     *
     * This method queries the device for a suitable decoder for the given [format].
     * If the default decoder is "OMX.Exynos.avc.dec", the method will search for
     * an alternative decoder that supports the "video/avc" MIME type, excluding those
     * whose names start with "OMX.Exynos".
     *
     * <b>Note:</b> The explicit check for the "OMX.Exynos.avc.dec" decoder is due
     * to known issues with eShare in eSight 4 when running on devices with the Exynos processor.
     *
     * @param format The [MediaFormat] specifying the format of the media data to be decoded.
     * @return A [MediaCodec] instance suitable for decoding the specified media format.
     * @throws IllegalArgumentException if no suitable decoder is found.
     */
    override fun getDecoder(format: MediaFormat): MediaCodec {
        Log.i(TAG, "getDecoder: Getting decoder")
        val mediaCodecList = MediaCodecList(MediaCodecList.ALL_CODECS)
        val decoder = mediaCodecList.findDecoderForFormat(format)
        if (decoder != "OMX.Exynos.avc.dec") {
            return MediaCodec.createByCodecName(decoder)
        }

        val codecs = mediaCodecList.codecInfos.filter {
            !it.isEncoder && !it.name.startsWith("OMX.Exynos")
        }.flatMap {
            it.supportedTypes.filter { mMime -> mMime == "video/avc" }.map { _ -> it.name }
        }
        return MediaCodec.createByCodecName(codecs.first())
    }

    companion object {
        private const val TIMEOUT = 10000L
    }
}