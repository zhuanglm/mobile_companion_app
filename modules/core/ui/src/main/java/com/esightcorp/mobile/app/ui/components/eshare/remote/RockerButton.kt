package com.esightcorp.mobile.app.ui.components.eshare.remote


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R




/**
 * Represents an Rocker button with 2 customizable icons.
 *
 * @param onClick Action to be performed when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param size Size of the oblong button. Width is derived from the height to maintain the oblong shape.
 * @param contentDescription Description of the icon for accessibility purposes.
 * @param painter Painter object for rendering the icon.
 * @param borderColor Color of the button's border.
 * @param backgroundColor Background color of the button.
 * @param iconTint Tint color of the icon.
 */
@Composable
fun RockerButton(
    modifier: Modifier = Modifier,
    onVolumeUpPressed: () -> Unit = {Unit},
    onVolumeDownPressed: () -> Unit = {Unit},
    size: Dp = DefaultOblongButtonHeight,
    contentDescription: String? = DefaultContentDescription,
    painter1: Painter = painterResource(id = DefaultIconResource),
    painter2: Painter = painterResource(id = DefaultIconResource),
    borderColor: Color = MaterialTheme.colors.secondaryVariant,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    iconTint: Color = Color.Black
) {
    val height = size * OblongWidthFactor
    val rockerShape = RoundedCornerShape(percent = 50)
    val iconSize = size * IconScalingFactor

    Surface(
        modifier = modifier
            .height(height)
            .width(size)
            .clip(rockerShape)
            .background(backgroundColor, rockerShape)
            .border(DefaultBorderWidth, borderColor, rockerShape),
        shape = rockerShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            // Volume Up
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clickable { onVolumeUpPressed() }
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Icon(
                    painter = painter1,
                    tint = iconTint,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }

            // Volume Down
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clickable { onVolumeDownPressed() }
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painter2,
                    tint = iconTint,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}


@Preview
@Composable
fun RockerButtonPreview() {
    Surface{
        RockerButton()
    }
}

// Additional constants
private val DefaultOblongButtonHeight = 100.dp
private const val OblongWidthFactor = 2.6f // Adjust this factor to change the oblong shape
private val DefaultButtonSize = 25.dp
private val DefaultContentDescription: String? = null
private val DefaultIconResource = R.drawable.round_question_mark_24
private val DefaultBorderWidth = 2.dp
private val DefaultElevation = 2.dp
private val PressedElevation = 4.dp
private val DisabledElevation = 1.dp
private val FocusedElevation = 3.dp
private val DefaultPadding = 0.dp
private const val IconScalingFactor = 0.8f
