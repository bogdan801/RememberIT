package com.bogdan801.rememberit.presentation.util

import androidx.compose.ui.graphics.Color
import com.google.android.material.math.MathUtils

/**
 * Допоміжна функція для інтерполяції між двома кольорами
 * @param start початковий колір
 * @param end кінцевий колір
 * @param state стан від 0 до 1
 * @return новий колір між початковим [start] і кінцевим [end] за станом [state]
 */
fun interpolateColor(start: Color, end: Color, state: Float): Color {
    return Color(
        MathUtils.lerp(start.red,   end.red,   state),
        MathUtils.lerp(start.green, end.green, state),
        MathUtils.lerp(start.blue,  end.blue,  state),
        MathUtils.lerp(start.alpha, end.alpha, state)
    )
}
