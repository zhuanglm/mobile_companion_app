/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.eshare.remote

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.extensions.accessibilityClickOnEvent
import com.esightcorp.mobile.app.ui.extensions.gestureHandler
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Represents a circular button with a customizable icon.
 *
 * @param modifier Modifier to be applied to the button.
 * @param size Diameter of the circular button.
 * @param contentDescription Description of the icon for accessibility purposes.
 * @param iconId Painter object for rendering the icon.
 */
@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    onDownEvent: OnActionCallback? = null,
    onUpEvent: OnActionCallback? = null,
    size: Dp = DefaultButtonSize,
    contentDescription: String? = null,
    @DrawableRes iconId: Int = DefaultIconResource,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    iconTint: Color = Color.Black,
    contentPadding: PaddingValues = PaddingValues(),
    border: BorderStroke? = null
) {
    val coroutineScope = rememberCoroutineScope()

    ElevatedButton(
        modifier = modifier
            .accessibilityClickOnEvent(coroutineScope,
                onDownEvent = onDownEvent,
                onUpEvent = onUpEvent)
            .size(size)
            .clip(CircleShape)
            .gestureHandler(onDownEvent, onUpEvent),
        shape = CircleShape,
        onClick = { },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = DefaultElevation,
            pressedElevation = PressedElevation,
            disabledElevation = DisabledElevation,
            focusedElevation = FocusedElevation
        ),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = border,
        contentPadding = PaddingValues(DefaultPadding)
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            val iconSize = size * IconScalingFactor

            Icon(
                painter = rememberDrawablePainter(
                    AppCompatResources.getDrawable(LocalContext.current, iconId)
                ),
                tint = iconTint,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

/**
 * Represents a smaller variant of the [CircleButton].
 */
@Composable
fun TinyCircleButton(
    modifier: Modifier = Modifier,
    onDownEvent: OnActionCallback? = null,
    onUpEvent: OnActionCallback? = null,
    @DrawableRes icon: Int = DefaultIconResource,
    contentPadding: PaddingValues = PaddingValues(),
    contentDescription: String? = null,
) {
    CircleButton(
        onDownEvent = onDownEvent,
        onUpEvent = onUpEvent,
        size = TinyButtonSize,
        contentDescription = contentDescription,
        modifier = modifier,
        iconId = icon,
        contentPadding = contentPadding,
    )
}

/**
 * Represents a regular-sized variant of the [CircleButton].
 */
@Composable
fun RegularCircleButton(
    modifier: Modifier = Modifier,
    onDownEvent: OnActionCallback? = null,
    onUpEvent: OnActionCallback? = null,
    size: Dp = RegularButtonSize,
    contentDescription: String? = null,
    @DrawableRes icon: Int = DefaultIconResource,
    contentPadding: PaddingValues = PaddingValues(),
) {
    CircleButton(
        modifier = modifier,
        onDownEvent = onDownEvent,
        onUpEvent = onUpEvent,
        size = size,
        contentDescription = contentDescription,
        iconId = icon,
        contentPadding = contentPadding,
    )
}

@Composable
fun ColorContrastButton(
    modifier: Modifier = Modifier,
    onClick: OnActionCallback? = null,
    primaryColor: Color,
    secondaryColor: Color,
    @DrawableRes icon: Int = DefaultIconResource,
    contentDescription: String? = null,
    size: Dp = RegularButtonSize
) {
    CircleButton(
        modifier = modifier.clickable(onClick = onClick?: {}),
        onDownEvent = onClick,
        backgroundColor = secondaryColor,
        iconTint = primaryColor,
        iconId = icon,
        size = size,
        contentDescription = contentDescription,
        border = BorderStroke(2.dp, primaryColor)
    )
}


@Preview
@Composable
internal fun ColorContrastPreview() {
    Surface {
        ColorContrastButton(primaryColor = Color.Black, secondaryColor = Color.Red)
    }
}

@Preview
@Composable
internal fun TinyCircleButtonPreview() {
    Surface {
        TinyCircleButton()
    }
}

@Preview
@Composable
internal fun RegularCircleButtonPreview() {
    Surface {
        RegularCircleButton(icon = R.drawable.arrow_down_button)
    }
}

// Constants for externalized values
private val DefaultButtonSize = 25.dp
private val DefaultIconResource = R.drawable.round_question_mark_24
private val DefaultElevation = 2.dp
private val PressedElevation = 4.dp
private val DisabledElevation = 1.dp
private val FocusedElevation = 3.dp
private val DefaultPadding = 0.dp
private const val IconScalingFactor = 0.7f
private val TinyButtonSize = 40.dp
private val RegularButtonSize = 75.dp