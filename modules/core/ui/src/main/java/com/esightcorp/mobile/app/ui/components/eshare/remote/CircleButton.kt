package com.esightcorp.mobile.app.ui.components.eshare.remote

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
 * Represents a circular button with a customizable icon.
 *
 * @param modifier Modifier to be applied to the button.
 * @param size Diameter of the circular button.
 * @param contentDescription Description of the icon for accessibility purposes.
 * @param painter Painter object for rendering the icon.
 */
@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    onDownEvent: OnActionCallback? = null,
    onUpEvent: OnActionCallback? = null,
    size: Dp = DefaultButtonSize,
    contentDescription: String? = null,
    painter: Painter = painterResource(DefaultIconResource),
    borderColor: Color = MaterialTheme.colors.secondaryVariant,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    iconTint: Color = Color.Black
) {
    ElevatedButton(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .gestureHandler(onDownEvent, onUpEvent),
        shape = CircleShape,
        border = BorderStroke(DefaultBorderWidth, borderColor),
        onClick = { },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = DefaultElevation,
            pressedElevation = PressedElevation,
            disabledElevation = DisabledElevation,
            focusedElevation = FocusedElevation
        ),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(DefaultPadding)
    ) {
        val iconSize = size * IconScalingFactor
        Icon(
            painter = painter,
            tint = iconTint,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )
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
    icon: Painter = painterResource(DefaultIconResource)
) {
    CircleButton(
        onDownEvent = onDownEvent,
        onUpEvent = onUpEvent,
        size = TinyButtonSize,
        modifier = modifier,
        painter = icon
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
    icon: Painter = painterResource(DefaultIconResource)
) {
    CircleButton(
        modifier = modifier,
        onDownEvent = onDownEvent,
        onUpEvent = onUpEvent,
        size = size,
        painter = icon
    )
}

@Composable
fun ColorContrastButton(
    modifier: Modifier = Modifier,
    onClick: OnActionCallback? = null,
    primaryColor: Color,
    secondaryColor: Color,
    icon: Painter = painterResource(DefaultIconResource),
    size: Dp = RegularButtonSize
) {
    CircleButton(
        modifier = modifier,
        onDownEvent = onClick,
        borderColor = primaryColor,
        backgroundColor = secondaryColor,
        iconTint = primaryColor,
        painter = icon,
        size = size
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
        RegularCircleButton()
    }
}

// Constants for externalized values
private val DefaultButtonSize = 25.dp
private val DefaultIconResource = R.drawable.round_question_mark_24
private val DefaultBorderWidth = 2.dp
private val DefaultElevation = 2.dp
private val PressedElevation = 4.dp
private val DisabledElevation = 1.dp
private val FocusedElevation = 3.dp
private val DefaultPadding = 0.dp
private const val IconScalingFactor = 0.8f
private val TinyButtonSize = 50.dp
private val RegularButtonSize = 75.dp