package com.esightcorp.mobile.app.ui.components.eshare.remote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R

@Composable
fun EshareRemote(
    modifier: Modifier = Modifier,
    onFinderButtonPressedEventDown: () -> Unit = { },
    onFinderButtonPressedEventUp: () -> Unit = { },
    onBluetoothButtonPressedEventDown: () -> Unit = { },
    onBluetoothButtonPressedEventUp: () -> Unit = { },
    onModeButtonPressedEventDown: () -> Unit = { },
    onModeButtonPressedEventUp: () -> Unit = { },
    onUpButtonPressedEventUp: () -> Unit = { },
    onUpButtonPressedEventDown: () -> Unit = { },
    onDownButtonPressedEventUp: () -> Unit = { },
    onDownButtonPressedEventDown: () -> Unit = { },
    onVolumeUpButtonPressedEventUp: () -> Unit = { },
    onVolumeUpButtonPressedEventDown: () -> Unit = { },
    onVolumeDownButtonPressedEventUp: () -> Unit = { },
    onVolumeDownButtonPressedEventDown: () -> Unit = { },
    onMenuButtonPressedEventUp: () -> Unit = { },
    onMenuButtonPressedEventDown: () -> Unit = { },
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        shape = RoundedCornerShape(0.9f),
        color = Color.Gray
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                //row for little round buttons
                TinyCircleButton(
                    onDownEvent = onFinderButtonPressedEventDown,
                    onUpEvent = onFinderButtonPressedEventUp,
                    icon = painterResource(
                        id = R.drawable.round_qr_code_24
                    )
                )
                TinyCircleButton(
                    onDownEvent = onBluetoothButtonPressedEventDown,
                    onUpEvent = onBluetoothButtonPressedEventUp,
                    icon = painterResource(
                        id = R.drawable.baseline_bluetooth_24
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {//row for the mode button
                OvalButton(
                    onDownEvent = onModeButtonPressedEventDown,
                    onUpEvent = onModeButtonPressedEventUp,
                    painter = painterResource(id = R.drawable.circle_mode_button)
                )
            }
            VolumeRockerAndUpDownButtons(
                onUpButtonPressedEventUp = onUpButtonPressedEventUp,
                onUpButtonPressedEventDown = onUpButtonPressedEventDown,
                onDownButtonPressedEventUp = onDownButtonPressedEventUp,
                onDownButtonPressedEventDown = onDownButtonPressedEventDown,
                onVolumeDownButtonPressedEventUp = onVolumeDownButtonPressedEventUp,
                onVolumeDownButtonPressedEventDown = onVolumeDownButtonPressedEventDown,
                onVolumeUpButtonPressedEventDown = onVolumeUpButtonPressedEventDown,
                onVolumeUpButtonPressedEventUp = onVolumeUpButtonPressedEventUp
            )
            Row {//row for menu button
                OvalButton(
                    onDownEvent = onMenuButtonPressedEventDown,
                    onUpEvent = onMenuButtonPressedEventUp, 
                    painter = painterResource(id = R.drawable.menu_button)
                )

            }
        }


    }
}

@Composable
fun VolumeRockerAndUpDownButtons(
    //TODO: Resizing this causes some issues. Need to debug this further
    size: Dp = 70.dp,
    onUpButtonPressedEventUp: () -> Unit = { Unit },
    onUpButtonPressedEventDown: () -> Unit = { Unit },
    onDownButtonPressedEventDown: () -> Unit = { Unit },
    onDownButtonPressedEventUp: () -> Unit = { Unit },
    onVolumeDownButtonPressedEventUp: () -> Unit = { Unit },
    onVolumeDownButtonPressedEventDown: () -> Unit = { Unit },
    onVolumeUpButtonPressedEventUp: () -> Unit = { Unit },
    onVolumeUpButtonPressedEventDown: () -> Unit = { Unit }

) {
    val circleButtonSpacerSize = size.times(0.5f)
    val betweenColumnSpacerSize = size.times(0.1f)
    val rockerButtonSize = size.times(1f)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        // Column for Circle Buttons
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegularCircleButton(
                size = size,
                onUpEvent = onUpButtonPressedEventUp,
                onDownEvent = onUpButtonPressedEventDown, 
                icon = painterResource(id = R.drawable.arrow_up_button)
            )
            Spacer(modifier = Modifier.height(circleButtonSpacerSize))
            RegularCircleButton(
                size = size,
                onUpEvent = onDownButtonPressedEventUp,
                onDownEvent = onDownButtonPressedEventDown,
                icon = painterResource(id = R.drawable.arrow_down_button)
            )
        }
        Spacer(modifier = Modifier.width(betweenColumnSpacerSize))

        // Rocker Button
        RockerButton(
            onVolumeDownEventDown = onVolumeDownButtonPressedEventDown,
            onVolumeDownEventUp = onVolumeDownButtonPressedEventUp,
            onVolumeUpEventDown = onVolumeUpButtonPressedEventDown,
            onVolumeUpEventUp = onVolumeUpButtonPressedEventUp,
            size = rockerButtonSize, 
            painter1 = painterResource(id = R.drawable.volume_up_button),
            painter2 = painterResource(id = R.drawable.volume_down_button)
        )
    }
}


@Preview
@Composable
fun VolumeRockerAndUpDownButtonsPreview() {
    Surface {
        VolumeRockerAndUpDownButtons(size = 100.dp)
    }

}

@Preview(name = "landscape", widthDp = 800, heightDp = 360)
@Composable
fun EshareRemotePreview() {
    Surface() {
        Row {
            Spacer(modifier = Modifier.weight(2f))
            EshareRemote(modifier = Modifier.weight(1f))
        }

    }
}