package com.bogdan801.rememberit.presentation.custom.composables

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.ui.theme.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Картка завдання
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    text: String = "",
    dueToDateTime: LocalDateTime,
    onCheckedChange: ((Boolean) -> Unit),
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit,
    done: Boolean
){
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val dueToColor = if(currentDateTime>dueToDateTime) MaterialTheme.colors.error else MaterialTheme.colors.primaryVariant

    val primary = MaterialTheme.colors.primary
    val onBackground = MaterialTheme.colors.onBackground

    val cardColor by animateColorAsState(targetValue = if(done) onBackground else primary, tween(durationMillis = 200))
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
                style = Typography.h4,
                color = MaterialTheme.colors.onPrimary
            )
            Box(modifier = Modifier.fillMaxSize()) {
                //line spacer
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterStart)
                        .background(color = MaterialTheme.colors.primaryVariant)
                )

                //due to date time
                Text(
                    text = stringResource(id = R.string.due_to),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    style = Typography.h6,
                    color = dueToColor
                )
                Text(
                    text =  "${dueToDateTime.hour}:${"%02d".format(dueToDateTime.minute)}\n" +
                            "${dueToDateTime.dayOfMonth}/${dueToDateTime.monthNumber}/${dueToDateTime.year}",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp),
                    style = Typography.h6,
                    color = dueToColor,
                    textAlign = TextAlign.End
                )

                //is done checkbox
                Checkbox(
                    modifier = Modifier.align(Alignment.Center),
                    checked = done,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(MaterialTheme.colors.secondary)
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
                        tint = MaterialTheme.colors.primaryVariant
                    )
                }
            }
        }
    }
}