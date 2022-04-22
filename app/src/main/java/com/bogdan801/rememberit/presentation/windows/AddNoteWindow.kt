package com.bogdan801.rememberit.presentation.windows

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.presentation.custom.composables.BottomSaveBar
import com.bogdan801.rememberit.presentation.custom.composables.TopAppBar
import com.bogdan801.rememberit.ui.theme.Typography
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun AddNoteWindow(){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                val customTextSelectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colors.secondary,
                    backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.2f)
                )
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    var notesTitleTextState by remember { mutableStateOf("") }
                    TextField(
                        value = notesTitleTextState,
                        onValueChange = {
                            notesTitleTextState = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        singleLine = false,
                        textStyle = Typography.h1,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            placeholderColor = MaterialTheme.colors.onSurface,
                            textColor = MaterialTheme.colors.onPrimary,
                            cursorColor = MaterialTheme.colors.onPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                        placeholder = { Text(text = "Title...", style = Typography.h1)}
                    )

                    var notesContentsTextState by remember { mutableStateOf("") }
                    TextField(
                        value = notesContentsTextState,
                        onValueChange = {
                            notesContentsTextState = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y=(-16).dp)
                            .padding(bottom = 80.dp),
                        singleLine = false,
                        textStyle = Typography.h4,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            placeholderColor = MaterialTheme.colors.onSurface,
                            textColor = MaterialTheme.colors.onPrimary,
                            cursorColor = MaterialTheme.colors.onPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                        placeholder = { Text(text = "Note...", style = Typography.h4)}
                    )
                }
            }

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