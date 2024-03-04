/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
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
fun BoldSubheader(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onSurface,
    textAlign: TextAlign? = TextAlign.Start
) {
    SubHeader(
        text = text,
        modifier = modifier,
        fontSize = 20.sp,
        color = color,
        textAlign = textAlign,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SubHeader(
    text: String,
    modifier: Modifier,
    fontSize: TextUnit = 25.sp,
    color: Color = MaterialTheme.colors.onSurface,
    textAlign: TextAlign? = TextAlign.Start,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = fontWeight,
        fontSize = fontSize,
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
    clickableText: String = "",
    clickableTextSuffix: String = ".",
    url: String = "",
    modifier: Modifier,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.onPrimary
) {

    val annotatedString = AnnotatedString.Builder().apply {
        append(text)
        //start of clickable part
        pushStringAnnotation(tag = "URL", annotation = url)
        pushStyle(
            SpanStyle(
                color = color,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
            )
        )
        append(clickableText)
        pop() //end of style
        pop() //end of annotation
        append(clickableTextSuffix)
    }.toAnnotatedString()
    ClickableText(
        text = annotatedString,
        modifier = modifier.semantics {
            onClick {
                onClick()
                true
            }
        },
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { _ ->
                    onClick()
                }
        },
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
    onTextLayoutResult: (TextLayoutResult?) -> Unit = { },
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign,
        onTextLayout = onTextLayoutResult
    )
}

@Composable
fun WrappableButtonText(
    text: String,
    semanticsContent: String? = null,
    modifier: Modifier,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Text(
        text = text,
        color = color,
        modifier = when (semanticsContent) {
            null -> modifier
            else -> modifier.semantics { contentDescription = semanticsContent }
        },
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
