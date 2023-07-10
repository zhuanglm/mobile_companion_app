package com.esightcorp.mobile.app.ui.components.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Yellow,
    primaryVariant = YellowVariant,
    onPrimary = DarkGrey,
    secondary = Color.White,
    onSecondary = DarkGrey,
    surface = DarkGrey,
    onSurface = Color.White,

)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Yellow,
    primaryVariant = YellowVariant,
    onPrimary = DarkGrey,
    secondary = Color.White,
    onSecondary = DarkGrey,
    surface = DarkGrey,
    onSurface = Color.White,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun Mobile_companion_appTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    if(darkTheme){
        systemUiController.setSystemBarsColor(
            color = DarkGrey,
            darkIcons = false
        )
    }else{
        //TODO:This needs to change if we want to support light themes
        /*
        Proper settings for light theme
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = true
         */
        systemUiController.setSystemBarsColor(
            color = DarkGrey,
            darkIcons = false
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}