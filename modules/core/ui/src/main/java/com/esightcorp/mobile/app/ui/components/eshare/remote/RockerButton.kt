package com.esightcorp.mobile.app.ui.components.eshare.remote


import androidx.compose.foundation.background
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


/**
 * Represents an Rocker button with 2 customizable icons.
 *
 * @param modifier Modifier to be applied to the button.
 * @param size Size of the oblong button. Width is derived from the height to maintain the oblong shape.
 * @param contentDescription Description of the icon for accessibility purposes.
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
    size: Dp = DefaultOblongButtonHeight,
    contentDescription: String? = null,
    painter1: Painter = painterResource(R.drawable.round_question_mark_24),
    painter2: Painter = painterResource(R.drawable.round_question_mark_24),
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
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize)
                )
            }

            // Volume Down
            Column(
                modifier = Modifier
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
                    contentDescription = contentDescription,
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
