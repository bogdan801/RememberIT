package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.ui.theme.RememberITTheme

/**
 * Картка повідомлення про пустий список
 */
@Composable
fun EmptyListMessage(
    modifier: Modifier = Modifier,
    text: String = "",
    size: Dp = 60.dp,
    iconPainter: Painter = painterResource(id = R.drawable.ic_empty_note)
){
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(size),
                painter = iconPainter,
                contentDescription = "Empty",
                tint = MaterialTheme.colors.primaryVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = text, color = MaterialTheme.colors.onSurface)
        }
    }
}