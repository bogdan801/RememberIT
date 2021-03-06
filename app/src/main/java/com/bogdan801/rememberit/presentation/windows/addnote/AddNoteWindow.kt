package com.bogdan801.rememberit.presentation.windows.addnote

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.presentation.custom.composables.BottomSaveBar
import com.bogdan801.rememberit.presentation.custom.composables.TopAppBar
import com.bogdan801.rememberit.ui.theme.Typography
import com.bogdan801.rememberit.data.mapper.toHumanReadableString

/**
 * This is Add/Edit window design
 * @param navController navigation controller
 * @param viewModel ViewModel for current window
 */
@Composable
fun AddNoteWindow(
    navController: NavHostController,
    selectedNoteId: Int = -1,
    viewModel: AddNoteViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    viewModel.initEditId(selectedNoteId)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ) {
        TopAppBar(
            title = if(selectedNoteId == -1) stringResource(id = R.string.creating_note) else stringResource(id = R.string.editing_note),
            onBackClick = {
                navController.popBackStack()
            },
            onUndoClick = {
                viewModel.undoClicked()
            },
            onRedoClick = {
                viewModel.redoClicked()
            },
            isUndoActive = viewModel.undoShowState.value,
            isRedoActive = viewModel.redoShowState.value
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
                    TextField(
                        value = viewModel.notesTitleTextState.value,
                        onValueChange = {
                            viewModel.notesTitleTextChanged(it)
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
                        placeholder = { Text(text = stringResource(id = R.string.title_placeholder), style = Typography.h1)}
                    )

                    TextField(
                        value = viewModel.notesContentsTextState.value,
                        onValueChange = {
                            viewModel.notesContentsTextChanged(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-16).dp)
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
                        placeholder = { Text(text = stringResource(id = R.string.note_placeholder), style = Typography.h4)}
                    )
                }
            }

            BottomSaveBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = viewModel.currentDateTime.toHumanReadableString(context),
                onSaveClick = {
                    if(viewModel.saveNoteClick()){
                        Toast.makeText(context, context.getText(R.string.saved), Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                    else Toast.makeText(context, context.getText(R.string.not_saved), Toast.LENGTH_SHORT).show()
                },
                context = context
            )
        }
    }
}