package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    switchState: MutableState<Boolean>,
    onStateChange: (Boolean) -> Unit = {},
    scale: Float = 1.2f,
    width: Dp = 36.dp,
    height: Dp = 20.dp,
    checkedTrackColor: Color = MaterialTheme.colors.secondary,
    uncheckedTrackColor: Color = MaterialTheme.colors.primaryVariant,
    thumbColor: Color = MaterialTheme.colors.primary,
    gapBetweenThumbAndTrackEdge: Dp = 3.dp
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

        // To move the thumb, we need to calculate the position (along x axis)
        val animatePosition = animateFloatAsState(
            targetValue = if (switchState.value)
                with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
            else
                with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
        )

        Canvas(
            modifier = Modifier
                .size(width = width, height = height)
                .scale(scale = scale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // This is called when the user taps on the canvas
                            switchState.value = !switchState.value
                            onStateChange.invoke(switchState.value)
                        }
                    )
                }
        ) {

            // Track
            drawRoundRect(
                color = if (switchState.value) checkedTrackColor else uncheckedTrackColor,
                cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx())
            )

            // Thumb
            drawCircle(
                color = thumbColor,
                radius = thumbRadius.toPx(),
                center = Offset(
                    x = animatePosition.value,
                    y = size.height / 2
                )
            )

        }
    }
}