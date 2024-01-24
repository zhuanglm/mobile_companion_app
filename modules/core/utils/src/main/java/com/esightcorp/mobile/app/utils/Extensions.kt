/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log

private const val tag = "Extension"

//region Context extensions

@SuppressLint("UnspecifiedRegisterReceiverFlag")
fun Context.safeRegisterReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(receiver, intentFilter)
        }
    } catch (ex: Throwable) {
        Log.e(tag, "Receiver register failed: ${ex.message}", ex)
    }
}

fun Context.safeUnregisterReceiver(receiver: BroadcastReceiver) {
    try {
        unregisterReceiver(receiver)
    } catch (ex: Throwable) {
        Log.w(tag, "Unregister receiver failed: ${ex.message}")
    }
}

//endregion
