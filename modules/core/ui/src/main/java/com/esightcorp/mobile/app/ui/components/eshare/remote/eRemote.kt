/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.eshare.remote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

@Composable
fun EshareRemote(
    modifier: Modifier = Modifier,
    onFinderButtonPressedEventDown: OnActionCallback? = null,
    onFinderButtonPressedEventUp: OnActionCallback? = null,
    onBluetoothButtonPressedEventDown: OnActionCallback? = null,
    onBluetoothButtonPressedEventUp: OnActionCallback? = null,
    onModeButtonPressedEventDown: OnActionCallback? = null,
    onModeButtonPressedEventUp: OnActionCallback? = null,
    onUpButtonPressedEventUp: OnActionCallback? = null,
    onUpButtonPressedEventDown: OnActionCallback? = null,
    onDownButtonPressedEventUp: OnActionCallback? = null,
    onDownButtonPressedEventDown: OnActionCallback? = null,
    onVolumeUpButtonPressedEventUp: OnActionCallback? = null,
    onVolumeUpButtonPressedEventDown: OnActionCallback? = null,
    onVolumeDownButtonPressedEventUp: OnActionCallback? = null,
    onVolumeDownButtonPressedEventDown: OnActionCallback? = null,
    onMenuButtonPressedEventUp: OnActionCallback? = null,
    onMenuButtonPressedEventDown: OnActionCallback? = null,
) {
    val configuration = LocalConfiguration.current
    val ratio: Float = configuration.screenHeightDp / DefaultScreenHeight

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        shape = RoundedCornerShape(0.9f),
        color = darkColors().background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //region Row 1
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f),
            ) {
                //row for little round buttons

                RegularCircleButton(
                    ratio = ratio,
                    size = TinyButtonSize,
                    //size = with(LocalDensity.current) { 15.dp.toPx() }.dp,
                    contentDescription = stringResource(id = R.string.kAccessibilityButtonFinder),
                    onUpEvent = onFinderButtonPressedEventUp,
                    onDownEvent = onFinderButtonPressedEventDown,
                    icon = R.drawable.finder_icon,
                    contentPadding = PaddingValues(3.dp),
                )

                RegularCircleButton(
                    ratio = ratio,
                    size = TinyButtonSize,
                    onUpEvent = onBluetoothButtonPressedEventUp,
                    onDownEvent = onBluetoothButtonPressedEventDown,
                    icon = R.drawable.baseline_bluetooth_24,
                    contentPadding = PaddingValues(3.dp),
                )
            }
            //endregion

            //region Row 2
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {//row for the mode button
                OvalButton(
                    ratio = ratio,
                    onDownEvent = onModeButtonPressedEventDown,
                    onUpEvent = onModeButtonPressedEventUp,
                    iconDrawableId = R.drawable.remote_button_home,
                    contentPadding = PaddingValues(3.dp),
                    contentDescription = stringResource(id = R.string.kAccessibilityButtonMode)
                )
            }
            //endregion

            //region Row 3
            VolumeRockerAndUpDownButtons(
                ratio,
                onUpButtonPressedEventUp = onUpButtonPressedEventUp,
                onUpButtonPressedEventDown = onUpButtonPressedEventDown,
                onDownButtonPressedEventUp = onDownButtonPressedEventUp,
                onDownButtonPressedEventDown = onDownButtonPressedEventDown,
                onVolumeDownButtonPressedEventUp = onVolumeDownButtonPressedEventUp,
                onVolumeDownButtonPressedEventDown = onVolumeDownButtonPressedEventDown,
                onVolumeUpButtonPressedEventDown = onVolumeUpButtonPressedEventDown,
                onVolumeUpButtonPressedEventUp = onVolumeUpButtonPressedEventUp
            )
            //endregion

            //region Row 4
            Row {//row for menu button
                OvalButton(
                    ratio = ratio,
                    onDownEvent = onMenuButtonPressedEventDown,
                    onUpEvent = onMenuButtonPressedEventUp,
                    iconDrawableId = R.drawable.menu_button,
                    contentPadding = PaddingValues(3.dp),
                    contentDescription = stringResource(id = R.string.kAccessibilityButtonMenu)
                )
            }
            //endregion
        }
    }
}

@Composable
fun VolumeRockerAndUpDownButtons(
    ratio: Float = 1f,
    onUpButtonPressedEventUp: OnActionCallback? = null,
    onUpButtonPressedEventDown: OnActionCallback? = null,
    onDownButtonPressedEventDown: OnActionCallback? = null,
    onDownButtonPressedEventUp: OnActionCallback? = null,
    onVolumeDownButtonPressedEventUp: OnActionCallback? = null,
    onVolumeDownButtonPressedEventDown: OnActionCallback? = null,
    onVolumeUpButtonPressedEventUp: OnActionCallback? = null,
    onVolumeUpButtonPressedEventDown: OnActionCallback? = null,
) {
    val size: Dp = UpDownButtonSize
    val circleButtonSpacerSize = size.times(ratio * 0.5f)
    val betweenColumnSpacerSize = size.times(ratio * 0.3f)
    val rockerButtonSize = size.times(ratio * 1f)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        // region Up-Down controller
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegularCircleButton(
                ratio = ratio,
                size = size,
                contentDescription = stringResource(id = R.string.kAccessibilityButtonUp),
                onUpEvent = onUpButtonPressedEventUp,
                onDownEvent = onUpButtonPressedEventDown,
                icon = R.drawable.arrow_up_button,
                contentPadding = PaddingValues(bottom = 5.dp),
            )
            Spacer(modifier = Modifier.height(circleButtonSpacerSize))
            RegularCircleButton(
                ratio = ratio,
                size = size,
                contentDescription = stringResource(id = R.string.kAccessibilityButtonDown),
                onUpEvent = onDownButtonPressedEventUp,
                onDownEvent = onDownButtonPressedEventDown,
                icon = R.drawable.arrow_down_button,
                contentPadding = PaddingValues(top = 5.dp),
            )
        }
        //endregion

        Spacer(modifier = Modifier.width(betweenColumnSpacerSize))

        //region Volume controller
        RockerButton(
            onVolumeDownEventDown = onVolumeDownButtonPressedEventDown,
            onVolumeDownEventUp = onVolumeDownButtonPressedEventUp,
            onVolumeUpEventDown = onVolumeUpButtonPressedEventDown,
            onVolumeUpEventUp = onVolumeUpButtonPressedEventUp,
            size = rockerButtonSize,
            firstContentDescription = stringResource(id = R.string.kAccessibilityButtonVolumeUp),
            secondContentDescription = stringResource(id = R.string.kAccessibilityButtonVolumeDown),
            backgroundColor = Color.White,
            painter1 = painterResource(R.drawable.volume_up_button),
            painter2 = painterResource(R.drawable.volume_down_button)
        )
        //endregion
    }
}


@Preview
@Composable
internal fun VolumeRockerAndUpDownButtonsPreview() = Surface {
    VolumeRockerAndUpDownButtons()
}

@Preview(name = "landscape", widthDp = 800, heightDp = 360)
@Preview(name = "pixel4", widthDp = 854, heightDp = 462)
@Composable
internal fun EshareRemotePreview() = MaterialTheme {
    Surface {
        Row {
            Spacer(modifier = Modifier.weight(2f))
            EshareRemote(modifier = Modifier.weight(1f))
        }
    }
}
