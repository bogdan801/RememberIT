package com.bogdan801.rememberit

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.bogdan801.rememberit.ui.theme.*
import com.bogdan801.rememberit.ui.windows.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RememberITTheme {
                Surface(color = MaterialTheme.colors.background) {
                    //NotesWindow()
                    AddNoteWindow()
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
    AddNoteWindow()
}