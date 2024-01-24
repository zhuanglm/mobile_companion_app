/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.text

import android.util.Log
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun Header1Text(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onSurface,
    textAlign: TextAlign? = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif,
        color = color,
        textAlign = textAlign
    )
}


@Composable
fun Subheader(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onSurface,
    textAlign: TextAlign? = TextAlign.Start
) {
    Log.d("TAG", "Subheader: $color")
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign,
        color = color
    )
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontFamily = FontFamily.SansSerif,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun FineText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontFamily = FontFamily.SansSerif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        textAlign = textAlign,
    )
}

@Composable
fun ClickableBodyText(
    text: String,
    modifier: Modifier,
    onClick: (Int) -> Unit,
    color: Color = MaterialTheme.colors.onPrimary
) {
    ClickableText(
        text = AnnotatedString(text),
        modifier = modifier,
        onClick = onClick,
        style = TextStyle(
            color = color,
            fontFamily = FontFamily.SansSerif,
            fontSize = 20.sp
        )
    )
}

@Composable
fun ButtonText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign,
    )
}

@Composable
fun WrappableButtonText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        fontFamily = FontFamily.SansSerif,
        softWrap = true
    )
}

@Composable
fun Button2Text(
    modifier: Modifier,
    text: String = "Text goes here",
    color: Color = MaterialTheme.colors.onPrimary,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        maxLines = 1,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun WrappableButton2Text(
    modifier: Modifier = Modifier,
    text: String = "Text goes here",
    color: Color = MaterialTheme.colors.onPrimary,
    textAlign: TextAlign = TextAlign.Center

) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        color = color,
        textAlign = textAlign
    )
}