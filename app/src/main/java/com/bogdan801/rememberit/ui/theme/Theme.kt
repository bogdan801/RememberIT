package com.bogdan801.rememberit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Gray70,
    primaryVariant = Gray60,
    secondary = Yellow,
    background = Gray80,
    error = RedErrorDark,
    onPrimary = Gray10,
    onBackground = Gray90,
    onSurface = Gray50
)

private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = Gray30,
    secondary = Yellow,
    background = Gray10,
    error = RedError,
    onPrimary = Color.Black,
    onBackground = Gray20,
    onSurface = Gray40
)

@Composable
fun RememberITTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
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