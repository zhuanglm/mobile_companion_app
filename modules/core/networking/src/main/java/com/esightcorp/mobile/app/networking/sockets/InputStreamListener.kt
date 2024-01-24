/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.networking.sockets

import java.io.InputStream

interface InputStreamListener {
    fun onInputStreamCreated(inputStream: InputStream)
    fun onInputStreamClosed()
    fun onInputStreamError()
}