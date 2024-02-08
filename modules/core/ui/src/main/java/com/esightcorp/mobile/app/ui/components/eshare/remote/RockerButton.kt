/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.eshare.remote


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.extensions.gestureHandler
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2


/**
 * Represents an Rocker button with 2 customizable icons.
 *
 * @param modifier Modifier to be applied to the button.
 * @param size Size of the oblong button. Width is derived from the height to maintain the oblong shape.
 * @param firstContentDescription Description of the icon for accessibility purposes.
 * @param secondContentDescription Description of the icon for accessibility purposes.
 * @param backgroundColor Background color of the button.
 * @param iconTint Tint color of the icon.
 */
@Composable
fun RockerButton(
    modifier: Modifier = Modifier,
    onVolumeUpEventDown: OnActionCallback? = null,
    onVolumeUpEventUp: OnActionCallback? = null,
    onVolumeDownEventDown: OnActionCallback? = null,
    onVolumeDownEventUp: OnActionCallback? = null,
    accessibilityTouchEvent: KSuspendFunction2<OnActionCallback?, OnActionCallback?, Unit>? = null,
    size: Dp = DefaultOblongButtonHeight,
    firstContentDescription: String? = null,
    secondContentDescription: String? = null,
    painter1: Painter = painterResource(R.drawable.round_question_mark_24),
    painter2: Painter = painterResource(R.drawable.round_question_mark_24),
    backgroundColor: Color = MaterialTheme.colors.secondary,
    iconTint: Color = Color.Black
) {
    val height = size * OblongWidthFactor
    val rockerShape = RoundedCornerShape(percent = 50)
    val iconSize = size * IconScalingFactor
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .height(height)
            .width(size)
            .clip(rockerShape),
        shape = rockerShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            verticalArrangement = Arrangement.Center,
        ) {
            // Volume Up
            Column(
                modifier = Modifier
                    .clickable(onClickLabel = firstContentDescription) {
                        coroutineScope.launch {
                            accessibilityTouchEvent?.invoke(onVolumeUpEventDown, onVolumeUpEventUp)
                        }
                    }
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .gestureHandler(onVolumeUpEventDown, onVolumeUpEventUp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painter1,
                    tint = iconTint,
                    contentDescription = firstContentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }

            // Volume Down
            Column(
                modifier = Modifier
                    .clickable(onClickLabel = secondContentDescription) {
                        coroutineScope.launch {
                            accessibilityTouchEvent?.invoke(onVolumeDownEventDown, onVolumeDownEventUp)
                        }
                    }
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .gestureHandler(onVolumeDownEventDown, onVolumeDownEventUp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painter2,
                    tint = iconTint,
                    contentDescription = secondContentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}


@Preview
@Composable
internal fun RockerButtonPreview() = Surface {
    RockerButton(backgroundColor = Color.Cyan)
}

// Additional constants
private val DefaultOblongButtonHeight = 100.dp
private const val OblongWidthFactor = 2.6f // Adjust this factor to change the oblong shape
private const val IconScalingFactor = 0.7f
