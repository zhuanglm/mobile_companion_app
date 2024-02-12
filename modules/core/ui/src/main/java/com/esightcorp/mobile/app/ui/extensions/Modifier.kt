/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.TOUCH_EVENT_DELAY
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Utility to help debugging a view's border
 */
fun Modifier.debugBorder(color: Color = Color.Red) = this.border(width = 1.dp, color = color)

/**
 * Gesture handler to support either:
 * - Quick tap
 * - Long press
 *
 * @param onGestureStarted Callback will be triggered when the view being tapped (equivalent to `MotionEvent.ACTION_DOWN`)
 * @param onGestureCompleted Callback triggered when the user completed the gesture (equivalent to `MotionEvent.ACTION_UP`)
 */
fun Modifier.gestureHandler(
    onGestureStarted: OnActionCallback? = null,
    onGestureCompleted: OnActionCallback? = null,
) = pointerInput(onGestureStarted, onGestureCompleted) {
    awaitEachGesture {
        awaitFirstDown(false).also {
            onGestureStarted?.invoke()
            it.consume()
        }

        waitForUpOrCancellation()?.consume()

        onGestureCompleted?.invoke()
    }
}

/**
 * Simulate tap down and up for TalkBack:
 *
 * @param scope CoroutineScope to run this simulation
 * @param onDownEvent Tap down event
 * @param onUpEvent Tap up event
 */
fun Modifier.accessibilityClickOnEvent(
    scope: CoroutineScope,
    contentDescription: String? = null,
    onDownEvent: OnActionCallback? = null,
    onUpEvent: OnActionCallback? = null,
) = this.clickable(onClickLabel = contentDescription) {
    scope.launch {
        onDownEvent?.invoke()

        if (onDownEvent != null && onUpEvent != null)
            delay(TOUCH_EVENT_DELAY)

        onUpEvent?.invoke()
    }
}