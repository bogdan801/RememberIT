package com.bogdan801.rememberit.ui.custom.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.ui.theme.RememberITTheme
import com.bogdan801.rememberit.ui.theme.Typography
import com.bogdan801.rememberit.ui.windows.SettingsWindow

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    itemIcon: @Composable () -> Unit,
    title: String,
    subtitle: String = "",
    additionalContent: @Composable () -> Unit = {},
    showTopSpacer: Boolean = false,
    showBottomSpacer: Boolean = false,
    onClick: () -> Unit = {}
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick)
    ){
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp),
                contentAlignment = Alignment.Center
            ){
                itemIcon.invoke()
            }
            
            Column {
                Text(text = title, style = Typography.h4, color = MaterialTheme.colors.onPrimary)
                if(subtitle != "")Text(text = subtitle, style = Typography.body1, color = MaterialTheme.colors.onSurface)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            additionalContent.invoke()
        }

        if(showTopSpacer){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colors.onBackground)
                    .align(Alignment.TopCenter)
            )
        }

        if(showBottomSpacer){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colors.onBackground)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreview() {
    RememberITTheme {
        SettingsItem(
            itemIcon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Clear all entries",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = "Clear all entries",
            subtitle = "I mean all of them",
            showTopSpacer = true,
            showBottomSpacer = true,
            additionalContent = {
                Text(text = "C", fontSize = 26.sp, color = Color.Black)
            }
        )
    }
}