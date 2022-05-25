package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.ui.theme.RememberITTheme
import com.bogdan801.rememberit.ui.theme.Typography

/**
 * Піделемент налаштувань
 */
@Composable
fun SettingsSubitem(
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    text: String = "",
    onClick: () -> Unit = {},
    additionalItem: @Composable () -> Unit = {},
    showTopSpacer: Boolean = false,
    showBottomSpacer: Boolean = false
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable(onClick = onClick)
            .background(color = MaterialTheme.colors.primary)
    ){
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp),
            text = text,
            style = Typography.body1,
            color = MaterialTheme.colors.onPrimary
        )

        Row(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 16.dp)
        ) {
            additionalItem.invoke()
        }

        if(showTopSpacer){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(MaterialTheme.colors.onBackground)
                    .align(Alignment.TopCenter)
            )
        }

        if(showBottomSpacer){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(MaterialTheme.colors.onBackground)
                    .align(Alignment.BottomCenter)
            )
        }
    }
    
}

@Preview(showBackground = true)
@Composable
fun SettingsSubitemPreview() {
    RememberITTheme {
        SettingsSubitem(
            text = "Default",
            showTopSpacer = true,
            showBottomSpacer = true,
            additionalItem = {
                ColorThemeTile()
            }
        )
    }
}