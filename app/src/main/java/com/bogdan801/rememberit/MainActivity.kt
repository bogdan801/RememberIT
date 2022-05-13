package com.bogdan801.rememberit

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.bogdan801.rememberit.data.datastore.readFromDataStore
import com.bogdan801.rememberit.presentation.navigation.Navigation
import com.bogdan801.rememberit.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            var darkTheme: Int?
            var colorTheme: Int?

            runBlocking {
                darkTheme = readFromDataStore("dark_theme")
                colorTheme = readFromDataStore("color_theme")
            }

            val defaultTheme = if(darkTheme == null) isSystemInDarkTheme() else darkTheme==1
            val darkThemeState = remember {
                mutableStateOf(defaultTheme)
            }

            var defaultColorTheme = ColorTheme.Default
            if(colorTheme != null){
                if(colorTheme in 0..ColorTheme.values().lastIndex){
                    defaultColorTheme = ColorTheme.values()[colorTheme!!]
                }
            }

            val colorThemeState = remember {
                mutableStateOf(defaultColorTheme)
            }

            RememberITTheme(darkTheme = darkThemeState.value, colorTheme = colorThemeState.value) {
                val systemUiController = rememberSystemUiController()

                val background = MaterialTheme.colors.background
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = background,
                        darkIcons = !darkThemeState.value
                    )
                }

                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Navigation(
                        navController = navController,
                        isDarkThemeState = darkThemeState,
                        colorThemeState = colorThemeState
                    )
                }
            }
        }
    }
}