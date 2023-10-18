package com.esightcorp.mobile.app.ui.components.eshare.remote

import android.view.MotionEvent
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R

/**
 * Represents a circular button with a customizable icon.
 *
 * @param onClick Action to be performed when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param size Diameter of the circular button.
 * @param contentDescription Description of the icon for accessibility purposes.
 * @param painter Painter object for rendering the icon.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircleButton(
    onDownEvent: () -> Unit,
    onUpEvent: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = DefaultButtonSize,
    contentDescription: String? = DefaultContentDescription,
    painter: Painter = painterResource(id = DefaultIconResource),
    borderColor: Color = MaterialTheme.colors.secondaryVariant,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    iconTint: Color = Color.Black
) {
    ElevatedButton(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onDownEvent()
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        onUpEvent()
                        true
                    }

                    else -> false
                }
            },
        shape = CircleShape,
        border = BorderStroke(DefaultBorderWidth, borderColor),
        onClick = { Unit },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = DefaultElevation,
            pressedElevation = PressedElevation,
            disabledElevation = DisabledElevation,
            focusedElevation = FocusedElevation
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
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
 *
 * @param onClickAction Action to be performed when the button is clicked.
 */
@Composable
fun TinyCircleButton(
    onDownEvent: () -> Unit = {},
    onUpEvent: () -> Unit = {},
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(id = DefaultIconResource)
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
 *
 * @param onClickAction Action to be performed when the button is clicked.
 */
@Composable
fun RegularCircleButton(
    onDownEvent: () -> Unit = {},
    onUpEvent: () -> Unit = {},
    modifier: Modifier = Modifier,
    size: Dp = RegularButtonSize,
    icon: Painter = painterResource(id = DefaultIconResource)
) {
    CircleButton(
        modifier = Modifier,
        onDownEvent = onDownEvent,
        onUpEvent = onUpEvent,
        size = size,
        painter = icon
    )
}

@Composable
fun ColorContrastButton(
    onDownEvent: () -> Unit = {},
    onUpEvent: () -> Unit = {},
    primaryColor: Color,
    secondaryColor: Color,
) {
    CircleButton(
        onDownEvent = onDownEvent,
        onUpEvent = onUpEvent,
        borderColor = primaryColor,
        backgroundColor = secondaryColor,
        iconTint = primaryColor
    )
}


@Preview
@Composable
fun ColorContrastPreview() {
    Surface {
        ColorContrastButton(primaryColor = Color.Black, secondaryColor = Color.Red)
    }

}

@Preview
@Composable
fun TinyCircleButtonPreview() {
    Surface {
        TinyCircleButton()
    }
}

@Preview
@Composable
fun RegularCircleButtonPreview() {
    Surface {
        RegularCircleButton()
    }
}

// Constants for externalized values
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
private val TinyButtonSize = 50.dp
private val RegularButtonSize = 75.dp