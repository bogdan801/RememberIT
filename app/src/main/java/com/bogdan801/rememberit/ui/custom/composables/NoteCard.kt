package com.bogdan801.rememberit.ui.custom.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.ui.theme.Gray30
import com.bogdan801.rememberit.ui.theme.Gray50
import kotlinx.datetime.LocalDateTime

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    titleText: String = "",
    noteText: String = "",
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit,
    lastEditDateTime: LocalDateTime
){
    Card(
        modifier = modifier,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().clickable(onClick = onClick)){
            //title and contents
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    text = titleText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = noteText,
                    modifier = Modifier.padding(top = 8.dp, bottom = 46.dp)
                )
            }

            //line spacer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .offset(y = (-50).dp)
                    .background(color = Gray30)
            )

            //bottom part with the date and delete button
            Text(
                text =
                    "${lastEditDateTime.hour}:${"%02d".format(lastEditDateTime.minute)}\n" +
                    "${lastEditDateTime.dayOfMonth}/${lastEditDateTime.month.value}/${lastEditDateTime.year}",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 12.dp),
                fontSize = 12.sp,
                color = Gray50
            )

            //delete button
            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.BottomEnd),
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete icon",
                    modifier = Modifier.size(25.dp),
                    tint = Gray30
                )
            }
        }

    }
}