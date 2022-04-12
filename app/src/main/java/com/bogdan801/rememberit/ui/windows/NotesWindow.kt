package com.bogdan801.rememberit.ui.windows

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.bogdan801.rememberit.tools.interpolateColor
import com.bogdan801.rememberit.ui.custom.composables.NoteCard
import com.bogdan801.rememberit.ui.custom.composables.SearchBar
import com.bogdan801.rememberit.ui.custom.composables.TaskCard
import com.bogdan801.rememberit.ui.custom.layouts.StaggeredVerticalGrid
import com.bogdan801.rememberit.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NotesWindow(){
    //context and focus manager
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    //current tab state
    val tabState by remember { mutableStateOf(0) }
    val pageState = rememberPagerState(pageCount = 2)
    val scope = rememberCoroutineScope()

    //navigation tabs color states and animations
    val secondary = MaterialTheme.colors.secondary
    val onPrimary = MaterialTheme.colors.onPrimary
    var notesColorState by remember { mutableStateOf(secondary) }
    var tasksColorState by remember { mutableStateOf(onPrimary) }

    //top navigation tab panel with settings icon
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        //navigation panel
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text (
                    text = "Notes",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                pageState.animateScrollToPage(0)
                            }
                        },
                    style = Typography.h2,
                    color = notesColorState
                )
                Text (
                    text = "Tasks",
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                pageState.animateScrollToPage(1)
                            }
                        },
                    style = Typography.h2,
                    color = tasksColorState,
                )
            }

            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = MaterialTheme.colors.secondary)
                    ) {
                        Toast
                            .makeText(context, "Settings", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .padding(10.dp)
                    .align(Alignment.CenterEnd)
            )
        }

        //search bar
        var searchBarTextState by remember { mutableStateOf("") }
        var searchPlaceholderState by remember { mutableStateOf("Search notes") }
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 10.dp),
            value = searchBarTextState,
            onValueChange = { newText->
                searchBarTextState = newText
            },
            placeholder = { Text(searchPlaceholderState, style = Typography.h5, color = MaterialTheme.colors.onSurface) },
            onSearch = {
                Toast.makeText(context, "Searching $searchBarTextState...", Toast.LENGTH_SHORT).show()
                focusManager.clearFocus()
            }
        )

        //scrollable panel with notes or tasks
        Box(modifier = Modifier.fillMaxSize()){
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pageState
            ) { index ->
                notesColorState = interpolateColor(MaterialTheme.colors.secondary, MaterialTheme.colors.onPrimary, pageState.currentPageOffset + pageState.currentPage)
                tasksColorState = interpolateColor(MaterialTheme.colors.onPrimary, MaterialTheme.colors.secondary, pageState.currentPageOffset + pageState.currentPage)

                if (pageState.currentPage == 0) searchPlaceholderState = "Search notes"
                if (pageState.currentPage == 1) searchPlaceholderState = "Search tasks"


                when(index){
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            StaggeredVerticalGrid {
                                NoteCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    titleText = "Нотатка для прикладу",
                                    noteText =  "Ця нотатка не містить жодної змістовної для вас інформації, була створена ддя прикладу і не має жодного сенсу для існування.",
                                    onClick = { Toast.makeText(context, "Editing", Toast.LENGTH_SHORT).show()},
                                    onDeleteClick = { Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()},
                                    lastEditDateTime = LocalDateTime(
                                        year = 2022,
                                        month = Month.APRIL,
                                        dayOfMonth = 10,
                                        hour = 11,
                                        minute = 22
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                    1 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            TaskCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                text = "Доробити нарешті цей додаток",
                                dueToDateTime = LocalDateTime(
                                    year = 2022,
                                    month = Month.MAY,
                                    dayOfMonth = 12,
                                    hour = 10,
                                    minute = 31
                                ),
                                onClick = { Toast.makeText(context, "Editing", Toast.LENGTH_SHORT).show()},
                                onDeleteClick = { Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()},
                                onCheckedChange = { Toast.makeText(context, "Checked: $it", Toast.LENGTH_SHORT).show()}
                            )
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }

            //add button

            Box(modifier = Modifier.size(150.dp).align(Alignment.BottomCenter).padding(bottom = 20.dp)){
                FloatingActionButton (
                    modifier = Modifier.align(Alignment.Center).size(70.dp),
                    onClick = { Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()},
                    shape = CircleShape,
                    backgroundColor = MaterialTheme.colors.secondary
                ){}
                Text(
                    text = "+",
                    modifier = Modifier.align(Alignment.Center).offset(y = (-2).dp),
                    color = MaterialTheme.colors.primary,
                    style = Typography.h1
                )
            }
        }
    }
}
