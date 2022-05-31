package com.bogdan801.rememberit.presentation.windows.settings

import android.content.Intent
import android.content.Intent.getIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.data.datastore.saveIntToDataStore
import com.bogdan801.rememberit.presentation.MainActivity
import com.bogdan801.rememberit.presentation.custom.composables.*
import com.bogdan801.rememberit.ui.theme.ColorTheme
import com.bogdan801.rememberit.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * This is settings window design
 * @param navController navigation controller
 * @param viewModel for current window
 * @param darkThemeState dark mode state
 * @param colorThemeState state of the color theme
 */
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
        title = stringResource(id = R.string.confirm_deletion),
        text = stringResource(id = R.string.do_you_want_to_delete),
        confirmButtonText = stringResource(id = R.string.confirm),
        dismissButtonText = stringResource(id = R.string.dismiss),
        onConfirmButtonClick = {
            viewModel.confirmDeleteClicked()
            Toast.makeText(context, context.getText(R.string.deleted_successfully), Toast.LENGTH_SHORT).show()
        }
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ){
        //top app bar
        TopAppBar(
            title = stringResource(id = R.string.settings),
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
                text = stringResource(id = R.string.all_settings),
                style = Typography.body2,
                color = MaterialTheme.colors.secondary
            )

            //dark mode settings item
            SettingsItem(
                itemIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_night_mode),
                        contentDescription = stringResource(id = R.string.dark_mode),
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(26.dp)
                    )
                },
                title = stringResource(id = R.string.dark_mode),
                subtitle = if(darkThemeState.value) stringResource(id = R.string.on) else stringResource(id = R.string.off),
                showBottomSpacer = true,
                onClick = {
                    darkThemeState.value = !darkThemeState.value
                    scope.launch {
                        context.saveIntToDataStore("dark_theme", if(darkThemeState.value) 1 else 0)
                    }
                },
                additionalContent = {
                    CustomSwitch(
                        switchState = darkThemeState,
                        onStateChange = {
                            scope.launch {
                                context.saveIntToDataStore("dark_theme", if(darkThemeState.value) 1 else 0)
                            }
                        }
                    )
                }
            )

            //color theme settings item

            SettingsItem(
                itemIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_palette),
                        contentDescription = stringResource(id = R.string.color_theme),
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(26.dp)
                    )
                },
                title = stringResource(id = R.string.color_theme),
                subtitle = colorThemeState.value.name,
                onClick = {
                    viewModel.visible.value = !viewModel.visible.value;
                },
                additionalContent = {
                    ColorThemeTile(colorTheme = colorThemeState.value)
                }
            )

            AnimatedVisibility(
                visible = viewModel.visible.value
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
                                    context.saveIntToDataStore("color_theme", ColorTheme.values().indexOf(it))
                                }

                                (context as ComponentActivity).recreate()
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
                        contentDescription = stringResource(id = R.string.clear_all_entries),
                        tint = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = stringResource(id = R.string.clear_all_entries),
                showTopSpacer = true,
                onClick = {
                    viewModel.showDeleteAllDialog()
                }
            )
        }
    }
}