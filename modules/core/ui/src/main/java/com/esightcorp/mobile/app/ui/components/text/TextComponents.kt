package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.text.ClickableText
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
    color: Color = Color.White
){
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif
    )
}


@Composable
fun Header2Text(
    text: String,
    modifier: Modifier,
    color: Color = Color.White,
    textAlign: TextAlign? = TextAlign.Justify
){
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign
    )
}
@Composable
fun BodyText(
    text: String, 
    modifier: Modifier, 
    color: Color = Color.White
){
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontFamily = FontFamily.SansSerif,
        fontSize = 20.sp
    )
}

@Composable
fun ClickableBodyText(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
    color: Color = Color.White
){
    ClickableText(
        text = AnnotatedString(text),
        modifier = modifier,
        onClick = {onClick},
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
    color: Color = Color.Black
){
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        fontFamily = FontFamily.SansSerif,
        maxLines = 1

    )
}

@Composable
fun Button2Text(
    text: String = "Text goes here",
    modifier: Modifier = Modifier,
    color: Color = Color.Black
){
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        maxLines = 1,
        color = color
    )
}