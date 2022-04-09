package com.bogdan801.rememberit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RememberITTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NotesWindow()
                }
            }
        }
    }
}

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
    var notesColorState by remember { mutableStateOf(Yellow) }
    var tasksColorState by remember { mutableStateOf(Color.Black) }

    //top navigation tab panel with settings icon
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        //navigation panel
        Box(modifier = Modifier.fillMaxWidth()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text (
                    text = "Notes",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                pageState.animateScrollToPage(0)
                            }
                        },
                    style = Typography.h1,
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
                    style = Typography.h1,
                    color = tasksColorState,
                )
            }

            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                tint = Yellow,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(42.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = Yellow)
                    ) {
                        Toast
                            .makeText(context, "Settings", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .padding(10.dp)
            )
        }

        //search bar
        var searchBarTextState by remember { mutableStateOf("") }
        var searchPlaceholderState by remember { mutableStateOf("Search notes")}
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 10.dp),
            value = searchBarTextState,
            onValueChange = { newText->
                searchBarTextState = newText
            },
            placeholder = {Text(searchPlaceholderState, color = Gray40)},
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
                notesColorState = interpolateColor(Yellow, Color.Black, pageState.currentPageOffset + pageState.currentPage)
                tasksColorState = interpolateColor(Color.Black, Yellow, pageState.currentPageOffset + pageState.currentPage)

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
                                    titleText = "Hgdeyugf shd s twd hdhddc",
                                    noteText =  "Rdfof yhy hyhjh de feu sjhhg ddskf sef essiuefhhs dfuseye fyu",
                                    onClick = {Toast.makeText(context, "Editing", Toast.LENGTH_SHORT).show()},
                                    onDeleteClick = {Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()},
                                    lastEditDateTime = LocalDateTime(
                                        year = 2020,
                                        month = Month.FEBRUARY,
                                        dayOfMonth = 1,
                                        hour = 16,
                                        minute = 21
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
                                    month = Month.MARCH,
                                    dayOfMonth = 3,
                                    hour = 15,
                                    minute = 5
                                ),
                                onClick = {Toast.makeText(context, "Editing", Toast.LENGTH_SHORT).show()},
                                onDeleteClick = {Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()},
                                onCheckedChange = {Toast.makeText(context, "Checked: $it", Toast.LENGTH_SHORT).show()}
                            )
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }

            //add button
            FloatingActionButton (
                modifier = Modifier
                    .padding(20.dp)
                    .size(70.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    Toast
                        .makeText(context, "puk", Toast.LENGTH_SHORT)
                        .show()
                },
                shape = CircleShape,
                backgroundColor = Yellow,
                contentColor = Color.White
            ) {
                Text(
                    text = "+",
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-16).dp),
                    fontSize = 100.sp,
                    fontFamily = dongleFontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

//preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesWindow()
}