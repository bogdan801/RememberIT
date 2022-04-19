package com.bogdan801.rememberit.ui.windows

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.bogdan801.rememberit.ui.custom.composables.BottomSaveBar
import com.bogdan801.rememberit.ui.custom.composables.TopAppBar

@Composable
fun AddNoteWindow(){
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ) {
        TopAppBar(
            title = "Creating note",
            onBackClick = {
                Toast.makeText(context, "Going back", Toast.LENGTH_SHORT).show()
            },
            onUndoClick = {
                Toast.makeText(context, "Undoing", Toast.LENGTH_SHORT).show()
            },
            onRedoClick = {
                Toast.makeText(context, "Redoing", Toast.LENGTH_SHORT).show()
            }
        )

        Box(modifier = Modifier.fillMaxSize()){
            BottomSaveBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "19 April 2022 14:05",
                onSaveClick = {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                },
                onTextClick = {
                    Toast.makeText(context, "Text", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AddNoteWindow()
}