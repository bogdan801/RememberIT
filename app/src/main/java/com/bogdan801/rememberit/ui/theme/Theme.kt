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
    onSurface = Gray50,
    surface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = Gray30,
    secondary = Yellow,
    background = Gray10,
    error = RedError,
    onPrimary = Color.Black,
    onBackground = Gray20,
    onSurface = Gray40,
    surface = Gray70
)

enum class ColorTheme(val color: Color){
    Default(Yellow),
    Blue(com.bogdan801.rememberit.ui.theme.Blue),
    Green(com.bogdan801.rememberit.ui.theme.Green),
    Violet(com.bogdan801.rememberit.ui.theme.Violet),
    Red(com.bogdan801.rememberit.ui.theme.Red)
}

@Composable
fun RememberITTheme(darkTheme: Boolean = isSystemInDarkTheme(), colorTheme: ColorTheme = ColorTheme.Default, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette.copy(
            secondary = colorTheme.color
        )
    } else {
        LightColorPalette.copy(
            secondary = colorTheme.color
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}