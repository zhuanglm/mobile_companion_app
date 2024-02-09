/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.eshare

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.esightcorp.mobile.app.ui.KEY_IS_ORIENTATION_CHANGING

/**
 * Rotate the screen to the expected orientation
 * @param context The current application's context
 * @param expectedOrientation The expected orientation
 * @param onRotatedResult Callback executed once the rotation is done. The return parameter is the original orientation (if any)
 */
fun rotateScreen(
    context: Context,
    expectedOrientation: Int,
    onRotatedResult: ((Int?) -> Unit)? = null,
) {
    val activity = context.findActivity() ?: run {
        onRotatedResult?.invoke(null)
        return
    }

    val originalOrientation = activity.requestedOrientation
    activity.requestedOrientation = expectedOrientation
    activity.intent?.putExtra(KEY_IS_ORIENTATION_CHANGING, true)

    onRotatedResult?.invoke(originalOrientation)
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
