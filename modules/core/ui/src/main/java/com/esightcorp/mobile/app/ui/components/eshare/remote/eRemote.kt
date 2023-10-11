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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EshareRemote(
    modifier: Modifier = Modifier,
    onFinderButtonPressed: () -> Unit = { },
    onBluetoothButtonPressed: () -> Unit = {  },
    onModeButtonPressed: () -> Unit = {  },
    onUpButtonPressed: () -> Unit = {  },
    onDownButtonPressed: () -> Unit = {  },
    onVolumeUpButtonPressed: () -> Unit = {  },
    onVolumeDownButtonPressed: () -> Unit = {  },
    onMenuButtonPressed: () -> Unit = {  }
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
                TinyCircleButton(onClickAction = onFinderButtonPressed)
                TinyCircleButton(onClickAction = onBluetoothButtonPressed)
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {//row for the mode button
                OvalButton(onClick = onModeButtonPressed)
            }
            VolumeRockerAndUpDownButtons(
                onUpButtonPressed = onUpButtonPressed,
                onDownButtonPressed = onDownButtonPressed,
                onVolumeDownButtonPressed = onVolumeDownButtonPressed,
                onVolumeUpButtonPressed = onVolumeUpButtonPressed
            )
            Row {//row for menu button
                OvalButton(onClick = onMenuButtonPressed)

            }
        }


    }
}

@Composable
fun VolumeRockerAndUpDownButtons(
    //TODO: Resizing this causes some issues. Need to debug this further
    size: Dp = 70.dp,
    onUpButtonPressed: () -> Unit = { Unit },
    onDownButtonPressed: () -> Unit = { Unit },
    onVolumeDownButtonPressed: () -> Unit = { Unit },
    onVolumeUpButtonPressed: () -> Unit = { Unit }
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
            RegularCircleButton(size = size, onClickAction = onUpButtonPressed)
            Spacer(modifier = Modifier.height(circleButtonSpacerSize))
            RegularCircleButton(size = size, onClickAction = onDownButtonPressed)
        }
        Spacer(modifier = Modifier.width(betweenColumnSpacerSize))

        // Rocker Button
        RockerButton(
            onVolumeDownPressed = onVolumeDownButtonPressed,
            onVolumeUpPressed = onVolumeUpButtonPressed,
            size = rockerButtonSize
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
    Surface () {
        Row {
            Spacer(modifier = Modifier.weight(2f))
            EshareRemote(modifier = Modifier.weight(1f))
        }

    }
}