/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.components.text.ButtonText
import com.esightcorp.mobile.app.ui.extensions.debugBorder
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun LeadingIconTextButton(
    onClick: () -> Unit,
    modifier: Modifier,
    icon: ImageVector? = null,
    @DrawableRes iconDrawableId: Int? = null,
    iconContextDescription: String? = null,
    text: String,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(
            MaterialTheme.colors.primary, MaterialTheme.colors.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        when (icon) {
            null -> if (iconDrawableId != null) {
                Icon(
                    painter = rememberDrawablePainter(
                        AppCompatResources.getDrawable(LocalContext.current, iconDrawableId)
                    ),
                    iconContextDescription,
                )
            }

            else -> Icon(icon, contentDescription = iconContextDescription)
        }
        ButtonText(
            text = text, modifier = modifier
                .weight(1f)
                .offset(x = 12.dp)
        )
    }
}

@Composable
fun TextRectangularButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    textAlign: TextAlign? = null,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.elevatedButtonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary,
            disabledContainerColor = MaterialTheme.colors.primaryVariant,
            disabledContentColor = MaterialTheme.colors.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        ButtonText(
            text = text,
            modifier = modifier.weight(1f),
            textAlign = textAlign,
        )
    }
}

@Composable
fun OutlinedTextRectangularButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    textAlign: TextAlign? = null,
    textColor: Color? = null,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(4.dp, MaterialTheme.colors.primary),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        when (textColor) {
            null -> ButtonText(
                text, modifier.weight(1f),
                textAlign = textAlign,
            )

            else -> ButtonText(
                text, modifier.weight(1f),
                textAlign = textAlign,
                color = textColor,
            )
        }
    }
}

@Composable
fun TrailingIconButton(
    onClick: () -> Unit,
    modifier: Modifier,
    icon: ImageVector? = null,
    @DrawableRes iconDrawableId: Int? = null,
    iconContextDescription: String? = null,
    text: String,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(
            MaterialTheme.colors.primary, MaterialTheme.colors.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        ButtonText(
            text = text, modifier = modifier
                .weight(1f)

        )

        when (icon) {
            null -> if (iconDrawableId != null) {
                Icon(
                    painter = rememberDrawablePainter(
                        AppCompatResources.getDrawable(LocalContext.current, iconDrawableId)
                    ),
                    iconContextDescription,
                )
            }

            else -> Icon(icon, contentDescription = iconContextDescription)
        }
    }
}

@Preview
@Composable
private fun TextAndIconRectangularButtonPreview() {
    TrailingIconButton(
        onClick = { }, modifier = Modifier.debugBorder(), icon = Icons.Rounded.Check, text = "WPA/WPA2/WPA3"
    )
}

@Preview
@Composable
private fun IconAndTextRectangularButtonPreview() {
    LeadingIconTextButton(
        onClick = { }, modifier = Modifier, icon = Icons.Default.Star, text = "Preview"
    )
}
