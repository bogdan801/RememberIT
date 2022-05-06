package com.bogdan801.rememberit.presentation.util

import androidx.compose.ui.graphics.Color
import com.google.android.material.math.MathUtils

fun interpolateColor(start: Color, end: Color, state: Float): Color {
    return Color(
        MathUtils.lerp(start.red, end.red, state),
        MathUtils.lerp(start.green, end.green, state),
        MathUtils.lerp(start.blue, end.blue, state),
        MathUtils.lerp(start.alpha, end.alpha, state))
}
