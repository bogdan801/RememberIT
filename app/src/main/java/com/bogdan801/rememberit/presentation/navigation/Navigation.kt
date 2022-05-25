package com.bogdan801.rememberit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bogdan801.rememberit.presentation.windows.addnote.AddNoteWindow
import com.bogdan801.rememberit.presentation.windows.addtask.AddTaskWindow
import com.bogdan801.rememberit.presentation.windows.notes.NotesWindow
import com.bogdan801.rememberit.presentation.windows.settings.SettingsViewModel
import com.bogdan801.rememberit.presentation.windows.settings.SettingsWindow
import com.bogdan801.rememberit.ui.theme.ColorTheme
import com.google.accompanist.systemuicontroller.SystemUiController

/**
 * Функція управління навігацією додатку
 */
@Composable
fun Navigation(
    navController: NavHostController,
    isDarkThemeState: MutableState<Boolean>,
    colorThemeState: MutableState<ColorTheme>
){
    NavHost(navController = navController, startDestination = Screen.NotesScreen.route){
        composable(Screen.NotesScreen.route){
            NotesWindow(navController = navController)
        }

        composable(
            route = Screen.AddNoteScreen.route + "/{noteId}",
            arguments = listOf(
                navArgument("noteId"){
                    type = NavType.IntType
                }
            )
        ){ entry ->
            AddNoteWindow(navController = navController, selectedNoteId = entry.arguments!!.getInt("noteId"))
        }

        composable(
            route = Screen.AddTaskScreen.route + "/{taskId}",
            arguments = listOf(
                navArgument("taskId"){
                    type = NavType.IntType
                }
            )
        ){ entry ->
            AddTaskWindow(navController = navController, selectedTaskId = entry.arguments!!.getInt("taskId"))
        }

        composable(Screen.SettingsScreen.route){
            SettingsWindow(
                navController = navController,
                darkThemeState = isDarkThemeState,
                colorThemeState = colorThemeState
            )
        }
    }
}