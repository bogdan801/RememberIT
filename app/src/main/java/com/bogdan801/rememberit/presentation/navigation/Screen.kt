package com.bogdan801.rememberit.presentation.navigation

sealed class Screen(val route: String) {
    object NotesScreen    : Screen("notes"   )
    object AddNoteScreen  : Screen("addnote" )
    object AddTaskScreen  : Screen("addtask" )
    object SettingsScreen : Screen("settings")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
