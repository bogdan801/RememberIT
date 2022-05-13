package com.bogdan801.rememberit.presentation.custom.composables

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.ui.theme.Typography

@Composable
fun BottomSaveBar(
    modifier: Modifier = Modifier,
    text: String = "",
    onSaveClick: () -> Unit = {},
    onTextClick: () -> Unit = {},
    context: Context
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
        elevation = 6.dp,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .height(48.dp)
                    .padding(start = 16.dp)
                    .clickable(
                        onClick = onTextClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    )
            ){
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    text = text,
                    style = Typography.body1,
                    color = MaterialTheme.colors.onPrimary
                )
            }

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .height(48.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.primary
                ),
                shape = CircleShape
            ) {
                Text(text = context.getString(R.string.save), style = Typography.button)
            }
        }
    }
}