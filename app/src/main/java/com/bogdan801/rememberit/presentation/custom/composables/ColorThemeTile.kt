package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.ui.theme.*


enum class ColorTheme(val color: Color){
    Default(Yellow),
    Blue(com.bogdan801.rememberit.ui.theme.Blue),
    Green(com.bogdan801.rememberit.ui.theme.Green),
    Violet(com.bogdan801.rememberit.ui.theme.Violet)
}

@Composable
fun ColorThemeTile(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme = ColorTheme.Default,
    size: Dp = 20.dp
){
    Box(modifier = modifier
        .size(size = size)
        .clip(CircleShape)
        .background(color = colorTheme.color)
    )
}

@Preview(showBackground = true)
@Composable
fun ColorThemeTilePreview() {
    RememberITTheme {
        ColorThemeTile()
    }
}