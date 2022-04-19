package com.bogdan801.rememberit.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.R

val dongleFontFamily = FontFamily(
        Font(R.font.dongle_regular, weight = FontWeight.Normal),
        Font(R.font.dongle_bold, weight = FontWeight.Bold),
        Font(R.font.dongle_light, weight = FontWeight.Light),
)

val visbyRoundFontFamily = FontFamily(
        Font(R.font.visby_round_bold, weight = FontWeight.Bold),
)

val Typography = Typography(
        body1 = TextStyle(
                fontSize = 14.sp
        ),
        h1 = TextStyle(
                fontSize = 26.sp
        ),
        h2 = TextStyle(
                fontFamily = visbyRoundFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
        ),
        h3 = TextStyle(
                fontFamily = visbyRoundFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
        ),
        h4 = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
        ),
        h5 = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        ),
        h6 = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
        ),
        button = TextStyle(
                fontSize = 70.sp
        )
)