package com.bogdan801.rememberit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bogdan801.rememberit.presentation.windows.addnote.AddNoteWindow
import com.bogdan801.rememberit.presentation.windows.addtask.AddTaskWindow
import com.bogdan801.rememberit.presentation.windows.notes.NotesWindow
import com.bogdan801.rememberit.presentation.windows.settings.SettingsWindow

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.NotesScreen.route){
        composable(Screen.NotesScreen.route){
            NotesWindow()
        }

        composable(
            route = Screen.AddNoteScreen.route + "/{noteId}",
            arguments = listOf(
                navArgument("noteId"){
                    type = NavType.IntType
                    nullable = true
                }
            )
        ){
            AddNoteWindow()
        }

        composable(
            route = Screen.AddTaskScreen.route + "/{taskId}",
            arguments = listOf(
                navArgument("taskId"){
                    type = NavType.IntType
                    nullable = true
                }
            )
        ){
            AddTaskWindow()
        }

        composable(Screen.SettingsScreen.route){
            SettingsWindow()
        }
    }
}