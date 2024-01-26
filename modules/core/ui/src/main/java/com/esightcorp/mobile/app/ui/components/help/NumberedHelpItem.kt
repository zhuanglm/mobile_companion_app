/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esightcorp.mobile.app.ui.components.text.WrappableButtonText

fun Modifier.badgeLayout() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // based on the expectation of only one line of text
        val minPadding = placeable.height / 4

        val width = maxOf(placeable.width + minPadding, placeable.height)
        layout(width, placeable.height) {
            placeable.place((width - placeable.width) / 2, 0)
        }
    }
@Composable
fun HelpItemNumber(
    modifier: Modifier = Modifier,
    number: Int = 1,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier.padding(8.dp)) {
        Text(
            text = number.toString(),
            modifier = modifier.background(MaterialTheme.colors.primary, shape = CircleShape).badgeLayout(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = color,
        )
    }
}

@Composable
fun HelpItemText(
    text: String = "This is a test",
    color: Color = MaterialTheme.colors.secondary,
) {
    WrappableButtonText(
        text = text,
        modifier = Modifier,
        color = color
    )
}

@Preview
@Composable
fun NumberedHelpItem(
    modifier: Modifier = Modifier,
    number: Int = 1,
    text: String = "This is a test",
    textColor: Color = MaterialTheme.colors.onSurface,
    numberColor: Color = MaterialTheme.colors.onPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HelpItemNumber(
            number = number,
            modifier = Modifier,
            color = numberColor,
        )
        HelpItemText(
            text = text,
            color = textColor,
        )
    }
}





