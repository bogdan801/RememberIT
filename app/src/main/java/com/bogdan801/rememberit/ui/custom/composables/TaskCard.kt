package com.bogdan801.rememberit.ui.custom.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.ui.theme.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    text: String = "",
    dueToDateTime: LocalDateTime,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit,
    isDone: MutableState<Boolean> = remember {mutableStateOf(false)}
){
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val dueToColor = if(currentDateTime>dueToDateTime) RedError else Gray30

    var cardColorState by remember { mutableStateOf(Color.White) }
    val cardColor by animateColorAsState(targetValue = cardColorState, tween(durationMillis = 200))
    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 120.dp),
        backgroundColor = cardColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable(onClick = onClick)
                .padding(8.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(8.dp),
                fontSize = 18.sp,
                color = Gray80
            )
            Box(modifier = Modifier.fillMaxSize()) {
                //line spacer
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterStart)
                        .background(color = Gray30)
                )

                //due to date time
                Text(
                    text = "Due to:",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    fontSize = 12.sp,
                    color = dueToColor
                )
                Text(
                    text =  "${dueToDateTime.hour}:${"%02d".format(dueToDateTime.minute)}\n" +
                            "${dueToDateTime.dayOfMonth}/${dueToDateTime.monthNumber}/${dueToDateTime.year}",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp),
                    fontSize = 12.sp,
                    color = dueToColor,
                    textAlign = TextAlign.End
                )

                //is done checkbox
                Checkbox(
                    modifier = Modifier.align(Alignment.Center),
                    checked = isDone.value,
                    onCheckedChange = {
                        isDone.value = !isDone.value
                        cardColorState = if(isDone.value) Gray20 else Color.White
                        onCheckedChange?.invoke(it)
                    },
                    colors = CheckboxDefaults.colors(Yellow)
                )

                //delete button
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(8.dp, 8.dp),
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
}