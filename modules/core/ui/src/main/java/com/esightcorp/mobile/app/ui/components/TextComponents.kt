package com.esightcorp.mobile.app.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Header1Text(
    text: String, 
    modifier: Modifier
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
    modifier: Modifier
){
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
    )
}

@Composable
fun ButtonText(
    text: String,
    modifier: Modifier
){
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        fontFamily = FontFamily.SansSerif,
        maxLines = 1
    )
}

@Composable
fun Button2Text(
    text: String,
    modifier: Modifier
){
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        maxLines = 1
    )
}