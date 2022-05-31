package com.bogdan801.rememberit.presentation

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.bogdan801.rememberit.data.datastore.readIntFromDataStore
import com.bogdan801.rememberit.presentation.navigation.Navigation
import com.bogdan801.rememberit.domain.notifications.*
import com.bogdan801.rememberit.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import com.bogdan801.rememberit.R
import kotlinx.coroutines.delay
import kotlinx.datetime.*

/**
 * MainActivity class, entry point for an app
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        createNotificationChannel()

        setContent {
            var darkTheme: Int?
            var colorTheme: Int?

            runBlocking {
                darkTheme = readIntFromDataStore("dark_theme")
                colorTheme = readIntFromDataStore("color_theme")
            }

            val defaultTheme = if(darkTheme == null) isSystemInDarkTheme() else darkTheme==1
            val darkThemeState = remember { mutableStateOf(defaultTheme) }

            var defaultColorTheme = ColorTheme.Default
            if(colorTheme != null){
                if(colorTheme in 0..ColorTheme.values().lastIndex){
                    defaultColorTheme = ColorTheme.values()[colorTheme!!]
                }
            }

            when(colorTheme){
                0 -> setTheme(R.style.Theme_RememberIT)
                1 -> setTheme(R.style.BlueTheme)
                2 -> setTheme(R.style.GreenTheme)
                3 -> setTheme(R.style.VioletTheme)
                4 -> setTheme(R.style.RedTheme)
            }

            val colorThemeState = remember { mutableStateOf(defaultColorTheme)}

            RememberITTheme(darkTheme = darkThemeState.value, colorTheme = colorThemeState.value) {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.background
                )

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