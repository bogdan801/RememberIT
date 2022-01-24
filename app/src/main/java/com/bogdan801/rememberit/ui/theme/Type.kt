package com.bogdan801.rememberit.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.R

val fontFamily = FontFamily(
        Font(R.font.dongle_regular, weight = FontWeight.Normal),
        Font(R.font.dongle_bold, weight = FontWeight.Bold),
        Font(R.font.dongle_light, weight = FontWeight.Light),
)

val Typo = Typography(
        body1 = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        ),
        h1 = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 42.sp
        ),
        h2 = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        ),
        h3 = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
        )
)