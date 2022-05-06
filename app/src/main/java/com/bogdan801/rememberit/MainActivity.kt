package com.bogdan801.rememberit

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bogdan801.rememberit.presentation.navigation.Navigation
import com.bogdan801.rememberit.ui.theme.*
import com.bogdan801.rememberit.presentation.windows.settings.SettingsWindow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            RememberITTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Navigation(navController = navController)
                }
            }
        }
    }
}

//preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //NotesWindow()
    //AddNoteWindow()
    //AddTaskWindow()
    //SettingsWindow()
}