package com.esightcorp.mobile.app.ui.components.eshare.remote

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.extensions.gestureHandler
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.google.accompanist.drawablepainter.rememberDrawablePainter


/**
 * Represents an oblong button with a customizable icon.
 *
 * @param modifier Modifier to be applied to the button.
 * @param size Size of the oblong button. Width is derived from the height to maintain the oblong shape.
 * @param contentDescription Description of the icon for accessibility purposes.
 * @param borderColor Color of the button's border.
 * @param backgroundColor Background color of the button.
 * @param iconTint Tint color of the icon.
 */
@Composable
fun OvalButton(
    modifier: Modifier = Modifier,
    onDownEvent: OnActionCallback? = null,
    onUpEvent: OnActionCallback? = null,
    size: Dp = DefaultOblongButtonHeight, // Height of the oblong button
    contentDescription: String? = null,
    @DrawableRes iconDrawableId: Int = DefaultIconResource,
    borderColor: Color = MaterialTheme.colors.secondaryVariant,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    iconTint: Color = Color.Black,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val width = size * OblongWidthFactor // Calculating the width to maintain the oblong shape
    val ovalShape = GenericShape { it, _ -> addOval(Rect(Offset.Zero, it)) }

    ElevatedButton(
        modifier = modifier
            .width(width) // Setting the derived width
            .height(size) // Setting the provided height
            .clip(ovalShape)
            .gestureHandler(onDownEvent, onUpEvent),
        shape = ovalShape,
        border = BorderStroke(DefaultBorderWidth, borderColor),
        onClick = { },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = DefaultElevation,
            pressedElevation = PressedElevation,
            disabledElevation = DisabledElevation,
            focusedElevation = FocusedElevation
        ),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            val iconSize = size * IconScalingFactor
            Icon(
                painter = rememberDrawablePainter(
                    AppCompatResources.getDrawable(LocalContext.current, iconDrawableId)
                ),
                tint = iconTint,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Preview
@Composable
internal fun OblongButtonPreview() = Surface {
    OvalButton()
}


// Additional constants
private val DefaultOblongButtonHeight = 50.dp
private const val OblongWidthFactor = 1.6f // Adjust this factor to change the oblong shape
private val DefaultIconResource = R.drawable.round_question_mark_24
private val DefaultBorderWidth = 2.dp
private val DefaultElevation = 2.dp
private val PressedElevation = 4.dp
private val DisabledElevation = 1.dp
private val FocusedElevation = 3.dp
private const val IconScalingFactor = 0.8f
