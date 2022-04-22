package com.bogdan801.rememberit.ui.windows

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.ui.custom.composables.CustomSwitch
import com.bogdan801.rememberit.ui.custom.composables.SettingsItem
import com.bogdan801.rememberit.ui.custom.composables.TopAppBar
import com.bogdan801.rememberit.ui.theme.RememberITTheme
import com.bogdan801.rememberit.ui.theme.Typography

@Composable
fun SettingsWindow(){
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ){
        //top app bar
        TopAppBar(
            title = "Settings",
            onBackClick = {
                Toast.makeText(context, "Going back", Toast.LENGTH_SHORT).show()
            },
            showUndoRedo = false
        )

        //list of all settings
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            //title
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp),
                text = "All settings",
                style = Typography.body2,
                color = MaterialTheme.colors.secondary
            )

            //dark mode settings item
            val defaultTheme = isSystemInDarkTheme()
            var darkModeSubtitleState by remember { mutableStateOf(if(defaultTheme) "On" else "Off") }
            val darkModeSwitchState = remember {
                mutableStateOf(defaultTheme)
            }
            SettingsItem(
                itemIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_night_mode),
                        contentDescription = "Dark mode",
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(26.dp)
                    )
                },
                title = "Dark mode",
                subtitle = darkModeSubtitleState,
                showBottomSpacer = true,
                onClick = {
                    darkModeSwitchState.value = !darkModeSwitchState.value
                    darkModeSubtitleState = if(darkModeSwitchState.value) "On" else "Off"
                    Toast.makeText(context, "Mode changed", Toast.LENGTH_SHORT).show()
                },
                additionalContent = {
                    CustomSwitch(
                        switchState = darkModeSwitchState,
                        onStateChange = {
                            darkModeSubtitleState = if(it) "On" else "Off"
                            Toast.makeText(context, "Dark mode is ${darkModeSubtitleState.lowercase()}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )

            //color theme settings item
            SettingsItem(
                itemIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_palette),
                        contentDescription = "Change color theme",
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(26.dp)
                    )
                },
                title = "Color theme",
                subtitle = "Default",
                //showBottomSpacer = true,
                onClick = {
                    Toast.makeText(context, "Theme change click", Toast.LENGTH_SHORT).show()
                }
            )



            //clear all settings item
            SettingsItem(
                itemIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Clear all entries",
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = "Clear all entries",
                showTopSpacer = true,
                onClick = {
                    Toast.makeText(context, "Clearing everything", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SettingsWindowPreview() {
    RememberITTheme {
        SettingsWindow()
    }

}