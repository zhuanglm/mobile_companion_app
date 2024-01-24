/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.streaming

import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface
import java.io.Closeable

abstract class Decoder (
    protected val mSurface: Surface?
): Closeable {

    companion object{
        const val TAG = "Decoder"
        const val mMime = "video/avc"
        val mAccessUnit = byteArrayOf(0x00, 0x00, 0x00, 0x01)
        const val mWidth = 1920
        const val mHeight = 1080
    }

    protected abstract var mDecoder: MediaCodec?
    protected abstract var isConfigured: Boolean
    protected abstract var mMediaInfo: MediaCodec.BufferInfo
    protected var onSizeChanged: OnSizeChanged? = null

    interface OnSizeChanged{
        fun onSizeChanged(width: Int, height: Int)
    }

    abstract fun initializeDecoder()

    abstract override fun close()
    abstract fun readFrame(dataInput: ByteArray, length: Int)
    abstract fun getDecoder(format: MediaFormat): MediaCodec
}
