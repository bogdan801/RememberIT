package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.ui.theme.RememberITTheme

@Composable
fun SettingsSubitem(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    additionalItem: @Composable () -> Unit = {},
    showTopSpacer: Boolean = false,
    showBottomSpacer: Boolean = false
){
    Box(modifier = modifier.fillMaxWidth().height(48.dp))
    
}

@Preview(showBackground = true)
@Composable
fun SettingsSubitemPreview() {
    RememberITTheme {
        SettingsSubitem()
    }
}