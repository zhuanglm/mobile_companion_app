/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback


//TODO: Create previews for everything

/**
 * Displays an `ExtendedFloatingActionButton` with an icon and a text.
 * The button fills the maximum width of its parent.
 * The icon is displayed via the `SupportButtonIcon` composable.
 *
 * @param onClick A lambda function that will be triggered when the button is clicked.
 * @param modifier A [Modifier] applied to the button for layout and styling.
 * @param painter A [Painter] object that describes the vector graphic to be displayed as the button's icon.
 * @param text The text displayed on the button.
 * @param textColor The color of the text.
 *
 * @see ExtendedFloatingActionButton
 * @see SupportButtonIcon
 * @sample SupportButtonTemplatePreview
 */
@Composable
fun SupportButtonTemplate(
    modifier: Modifier = Modifier,
    onClick: OnActionCallback? = null,
    painter: Painter = painterResource(R.drawable.glasses),
    text: String = "Override me",
    textColor: Color = MaterialTheme.colors.onSurface,
    description: String? = null
) {
    ExtendedFloatingActionButton(
        text = { BodyText(text = text, modifier = modifier, color = textColor) },
        icon = {
            SupportButtonIcon(
                onClick = { }, modifier = modifier.clearAndSetSemantics { }, painter = painter,
                contentDescription = description
            )
        },
        onClick = { onClick?.invoke() },
        containerColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .semantics{ contentDescription = description ?: "" },
        elevation = FloatingActionButtonDefaults.elevation(
            dimensionResource(R.dimen.zero),
            dimensionResource(R.dimen.zero),
            dimensionResource(R.dimen.zero),
            dimensionResource(R.dimen.zero)
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun SupportButtonTemplatePreview() {
    MaterialTheme {
        SupportButtonTemplate(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(R.drawable.glasses),
            text = "Support Button",
            textColor = MaterialTheme.colors.onSurface
        )
    }
}


/**
 * Displays an icon button filled with the primary color and an icon.
 * The icon's size is defined by the dimension resource `R.dimen.filled_icon_bottom_button_size`.
 *
 * @param onClick A lambda function that will be triggered when the button is clicked.
 * @param modifier A [Modifier] applied to the button for layout and styling.
 * @param painter A [Painter] object that describes the vector graphic to be displayed on the button.
 *
 * @see FilledIconButton
 * @sample SupportButtonIconPreview
 */
@Composable
private fun SupportButtonIcon(
    onClick: OnActionCallback? = null,
    modifier: Modifier,
    painter: Painter,
    contentDescription: String?,
) {
    FilledIconButton(
        onClick = { onClick?.invoke() },
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary
        ),
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(dimensionResource(R.dimen.filled_icon_bottom_button_size))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SupportButtonIconPreview() {
    MaterialTheme {
        SupportButtonIcon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(R.drawable.glasses),
            contentDescription = stringResource(id = R.string.kFeedbackButtonText)
        )
    }
}
