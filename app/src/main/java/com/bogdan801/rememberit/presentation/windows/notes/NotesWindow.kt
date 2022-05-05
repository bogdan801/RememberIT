package com.bogdan801.rememberit.presentation.windows.notes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bogdan801.rememberit.util.interpolateColor
import com.bogdan801.rememberit.presentation.custom.composables.NoteCard
import com.bogdan801.rememberit.presentation.custom.composables.SearchBar
import com.bogdan801.rememberit.presentation.custom.composables.TaskCard
import com.bogdan801.rememberit.presentation.custom.layouts.StaggeredVerticalGrid
import com.bogdan801.rememberit.presentation.navigation.Screen
import com.bogdan801.rememberit.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NotesWindow(
    navController: NavHostController
){
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
                        navController.navigate(Screen.SettingsScreen.route)
                    }
                    .padding(10.dp)
                    .align(Alignment.CenterEnd)
            )
        }

        //search bar
        var searchBarTextState by remember { mutableStateOf("") }
        var searchPlaceholderState by remember { mutableStateOf("Search notes") }

        val customTextSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.2f)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors){
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
        }

        //scrollable panel with notes or tasks
        Box(modifier = Modifier.fillMaxSize()){
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pageState
            ) { index ->
                notesColorState = interpolateColor(MaterialTheme.colors.secondary, MaterialTheme.colors.onPrimary, pageState.currentPageOffset + pageState.currentPage)
                tasksColorState = interpolateColor(MaterialTheme.colors.onPrimary, MaterialTheme.colors.secondary, pageState.currentPageOffset + pageState.currentPage)

                SideEffect {
                    searchPlaceholderState = if (pageState.currentPage == 0) "Search notes" else "Search tasks"
                }

                when(index){
                    0 -> {
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
                            StaggeredVerticalGrid {
                                NoteCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    titleText = "Нотатка для прикладу",
                                    noteText =  "Нотатка для прикладу. Нотатка для прикладу. Нотатка для прикладу",
                                    onClick = {
                                        navController.navigate(Screen.AddNoteScreen.withArgs("0"))
                                    },
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
                                onClick = {
                                    navController.navigate(Screen.AddTaskScreen.withArgs("0"))
                                },
                                onDeleteClick = { Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()},
                                onCheckedChange = { Toast.makeText(context, "Checked: $it", Toast.LENGTH_SHORT).show()}
                            )
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }

            //add button
            Box(modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)){
                FloatingActionButton (
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(70.dp),
                    onClick = {
                        if(pageState.currentPage == 0) navController.navigate(Screen.AddNoteScreen.withArgs("-1"))
                        if(pageState.currentPage == 1) navController.navigate(Screen.AddTaskScreen.withArgs("-1"))
                    },
                    shape = CircleShape,
                    backgroundColor = MaterialTheme.colors.secondary
                ){}
                Text(
                    text = "+",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-2).dp),
                    color = MaterialTheme.colors.primary,
                    style = Typography.subtitle1
                )
            }
        }
    }
}
