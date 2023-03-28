package com.esightcorp.mobile.app.companion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.esightcorp.mobile.app.companion.ui.theme.*

private val DarkColorPalette = darkColors(
    primary = Yellow,
    primaryVariant = YellowVariant,
    secondary = Color.White,
    surface = DarkGrey
)

private val LightColorPalette = lightColors(
    primary = Green,
    primaryVariant = GreenVariant,
    secondary = Color.White,
    surface = DarkGrey

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
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}