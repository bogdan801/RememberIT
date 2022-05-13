package com.bogdan801.rememberit.presentation.windows.notes

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.rememberit.R
import com.bogdan801.rememberit.presentation.custom.composables.EmptyListMessage
import com.bogdan801.rememberit.presentation.util.interpolateColor
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NotesWindow(
    navController: NavHostController,
    notesViewModel: NotesViewModel = hiltViewModel()
){
    //context, focus manager, scope
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    //current tab state
    val pageState = rememberPagerState(pageCount = 2)

    //navigation tabs color states
    val secondary = MaterialTheme.colors.secondary
    val onPrimary = MaterialTheme.colors.onPrimary
    var notesColorState by remember { mutableStateOf(secondary) }
    var tasksColorState by remember { mutableStateOf(onPrimary) }

    //top navigation tab panel with settings icon
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        //navigation panel
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
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
        val customTextSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.2f)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors){
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 10.dp),
                value = notesViewModel.searchBarTextState.value,
                onValueChange = { newText->
                    notesViewModel.searchBarValueChanged(newText, pageState.currentPage)
                },
                placeholder = {
                    Text(
                        text = notesViewModel.searchPlaceholderState.value,
                        style = Typography.h5,
                        color = MaterialTheme.colors.onSurface
                    )
                },
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        }

        //scrollable panel with notes or tasks
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ){
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pageState
            ) { index ->
                notesColorState = interpolateColor(
                    MaterialTheme.colors.secondary,
                    MaterialTheme.colors.onPrimary,
                    pageState.currentPageOffset + pageState.currentPage
                )
                tasksColorState = interpolateColor(
                    MaterialTheme.colors.onPrimary,
                    MaterialTheme.colors.secondary,
                    pageState.currentPageOffset + pageState.currentPage)

                SideEffect {
                    notesViewModel.setPlaceholder(pageState.currentPage)
                }

                when(index) {
                    0 -> {
                        if(
                            (notesViewModel.searchBarTextState.value.isBlank() && notesViewModel.allNotesState.value.isEmpty())
                            ||
                            (notesViewModel.searchBarTextState.value.isNotBlank() && notesViewModel.foundNotesState.value.isEmpty())
                        ){
                            EmptyListMessage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 100.dp),
                                iconPainter = painterResource(id = R.drawable.ic_empty_note),
                                text = "There's no notes to show"
                            )
                        }
                        else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 10.dp)
                            ) {
                                StaggeredVerticalGrid {
                                    //not searching
                                    if(notesViewModel.searchBarTextState.value.isBlank()) {
                                        notesViewModel.allNotesState.value.forEach { note ->
                                            NoteCard(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp),
                                                titleText = note.title,
                                                noteText =  note.contents,
                                                onClick = {
                                                    navController.navigate(Screen.AddNoteScreen.withArgs(note.id.toString()))
                                                },
                                                onDeleteClick = {
                                                    notesViewModel.noteDeleteClick(note.id)
                                                },
                                                lastEditDateTime = note.dateTime
                                            )
                                        }
                                    }
                                    //searching
                                    else {
                                        notesViewModel.foundNotesState.value.forEach { note ->
                                            NoteCard(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp),
                                                titleText = note.title,
                                                noteText =  note.contents,
                                                onClick = {
                                                    navController.navigate(Screen.AddNoteScreen.withArgs(note.id.toString()))
                                                },
                                                onDeleteClick = {
                                                    notesViewModel.noteDeleteClick(note.id)
                                                },
                                                lastEditDateTime = note.dateTime
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(130.dp))
                            }
                        }
                    }
                    1 -> {
                        if(
                            (notesViewModel.searchBarTextState.value.isBlank() && notesViewModel.allTasksState.value.isEmpty())
                            ||
                            (notesViewModel.searchBarTextState.value.isNotBlank() && notesViewModel.foundTasksState.value.isEmpty())
                        ){
                            EmptyListMessage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 100.dp),
                                iconPainter = painterResource(id = R.drawable.ic_empty_task),
                                text = "There's no tasks to show"
                            )
                        }
                        else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                //not searching
                                if(notesViewModel.searchBarTextState.value.isBlank()) {
                                    notesViewModel.allTasksState.value.forEach { task ->
                                        TaskCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 4.dp),
                                            text = task.contents,
                                            dueToDateTime = task.dueTo,
                                            onClick = {
                                                navController.navigate(Screen.AddTaskScreen.withArgs(task.id.toString()))
                                            },
                                            onDeleteClick = {
                                                notesViewModel.taskDeleteClick(task.id)
                                            },
                                            onCheckedChange = {
                                                notesViewModel.taskCheckedChanged(task.id, it)
                                            },
                                            done = task.isChecked
                                        )
                                    }
                                }
                                //searching
                                else {
                                    notesViewModel.foundTasksState.value.forEach { task ->
                                        TaskCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 4.dp),
                                            text = task.contents,
                                            dueToDateTime = task.dueTo,
                                            onClick = {
                                                navController.navigate(Screen.AddTaskScreen.withArgs(task.id.toString()))
                                            },
                                            onDeleteClick = {
                                                notesViewModel.taskDeleteClick(task.id)
                                            },
                                            onCheckedChange = {
                                                notesViewModel.taskCheckedChanged(task.id, it)
                                            },
                                            done = task.isChecked
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(130.dp))
                            }
                        }
                    }
                }
            }

            //add button
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.BottomCenter)
            ){
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
