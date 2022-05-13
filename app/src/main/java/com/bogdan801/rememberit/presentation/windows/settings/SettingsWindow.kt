package com.bogdan801.rememberit.presentation.windows.settings

import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.data.datastore.saveToDataStore
import com.bogdan801.rememberit.presentation.custom.composables.*
import com.bogdan801.rememberit.ui.theme.ColorTheme
import com.bogdan801.rememberit.ui.theme.RememberITTheme
import com.bogdan801.rememberit.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsWindow(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
    darkThemeState: MutableState<Boolean>,
    colorThemeState: MutableState<ColorTheme>
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    DialogBox(
        showDialogState = viewModel.showDialogState,
        title = "Confirm deletion",
        text = "Do you really want to delete all notes and tasks?",
        confirmButtonText = "Confirm",
        dismissButtonText = "Dismiss",
        onConfirmButtonClick = {
            viewModel.confirmDeleteClicked()
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
        }
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ){
        //top app bar
        TopAppBar(
            title = "Settings",
            onBackClick = {
                navController.popBackStack()
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
                subtitle = if(darkThemeState.value) "On" else "Off",
                showBottomSpacer = true,
                onClick = {
                    darkThemeState.value = !darkThemeState.value
                    scope.launch {
                        context.saveToDataStore("dark_theme", if(darkThemeState.value) 1 else 0)
                    }
                },
                additionalContent = {
                    CustomSwitch(
                        switchState = darkThemeState,
                        onStateChange = {
                            scope.launch {
                                context.saveToDataStore("dark_theme", if(darkThemeState.value) 1 else 0)
                            }
                        }
                    )
                }
            )

            //color theme settings item
            var visible by remember { mutableStateOf(false) }
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
                subtitle = colorThemeState.value.name,
                onClick = {
                    visible = !visible;
                },
                additionalContent = {
                    ColorThemeTile(colorTheme = colorThemeState.value)
                }
            )

            AnimatedVisibility(
                visible = visible
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ColorTheme.values().forEach {
                        SettingsSubitem(
                            text = it.name,
                            showTopSpacer = true,
                            additionalItem = {
                                ColorThemeTile(colorTheme = it)
                            },
                            onClick = {
                                colorThemeState.value = it
                                scope.launch {
                                    context.saveToDataStore("color_theme", ColorTheme.values().indexOf(it))
                                }
                            }
                        )
                    }
                }
            }

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
                    viewModel.showDeleteAllDialog()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsWindowPreview() {
    RememberITTheme {
        //SettingsWindow()
    }

}