package com.bogdan801.rememberit.presentation.custom.composables

import com.bogdan801.rememberit.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.ui.theme.Typography

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: ()->Unit = {},
    showUndoRedo: Boolean = true,
    onUndoClick: ()->Unit = {},
    isUndoActive: Boolean = false,
    onRedoClick: ()->Unit = {},
    isRedoActive: Boolean = false
){
    //background panel
    Box(modifier = modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.BottomCenter
    ){
        //row with elements
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //back icon
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go Back",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(32.dp)
                )
            }

            //box with title and buttons
            Box(modifier = Modifier.fillMaxWidth()){
                //title
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = title,
                    style = Typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )

                //undo/redo buttons
                if(showUndoRedo){
                    Row(modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                    ){
                        //undo button
                        IconButton(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterVertically),
                            onClick = onUndoClick,
                            enabled = isUndoActive
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_undo),
                                contentDescription = "Undo",
                                tint = if(isUndoActive) MaterialTheme.colors.secondary
                                       else             MaterialTheme.colors.onSurface,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        //redo button
                        IconButton(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterVertically),
                            onClick = onRedoClick,
                            enabled = isRedoActive
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_redo),
                                contentDescription = "Redo",
                                tint = if(isRedoActive) MaterialTheme.colors.secondary
                                       else             MaterialTheme.colors.onSurface,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    TopAppBar(title = "Create note")
}