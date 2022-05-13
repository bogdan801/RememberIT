package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.ui.theme.Typography

@Composable
fun DialogBox(
    showDialogState: MutableState<Boolean>,
    title: String = "Do you confirm?",
    text: String = "",
    confirmButtonText: String = "Confirm",
    dismissButtonText: String = "Dismiss",
    onConfirmButtonClick: () -> Unit = {},
    onDismissButtonClick: () -> Unit = {}
) {
    if (showDialogState.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(text = title, style = Typography.h1, color = MaterialTheme.colors.onPrimary)
            },
            text = {
                Text(text, style = Typography.body1, color = MaterialTheme.colors.onPrimary)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmButtonClick.invoke()
                        showDialogState.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismissButtonClick.invoke()
                        showDialogState.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.background,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(dismissButtonText)
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )
    }
}