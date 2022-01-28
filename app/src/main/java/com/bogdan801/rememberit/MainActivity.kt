package com.bogdan801.rememberit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.rememberit.ui.theme.*
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

val sampleText = listOf(
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Viverra adipiscing at in tellus integer feugiat. Feugiat in ante metus dictum. Elit ut aliquam purus sit amet luctus. Nunc faucibus a pellentesque sit. Phasellus vestibulum lorem sed risus ultricies tristique.",
    "Augue ut lectus arcu bibendum at varius. Ut diam quam nulla porttitor massa id neque aliquam vestibulum.",
    "Tincidunt vitae semper quis lectus nulla at volutpat diam. Molestie a iaculis at erat pellentesque adipiscing commodo elit. Porttitor lacus luctus accumsan tortor posuere. Id interdum velit laoreet id donec ultrices tincidunt arcu non. Id aliquet risus feugiat in ante metus dictum at tempor.",
    "Rutrum tellus pellentesque eu tincidunt tortor aliquam.",
    "Risus ultricies tristique nulla aliquet enim tortor at auctor urna. Malesuada bibendum arcu vitae elementum curabitur vitae nunc sed velit. Maecenas volutpat blandit aliquam etiam. Vitae purus faucibus ornare suspendisse sed. Dignissim suspendisse in est ante in. Lacus laoreet non curabitur gravida. Adipiscing bibendum est ultricies integer quis auctor. ",
    "Nisl tincidunt eget nullam non nisi est sit. Consectetur lorem donec massa sapien faucibus et molestie ac. Eget nullam non nisi est sit amet facilisis. Sit amet mauris commodo quis. Eget arcu dictum varius duis at consectetur lorem donec massa."
)

@Composable
fun NotesWindow(){
    //context and focus manager
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    //current tab state
    var tabState by remember { mutableStateOf(0) }

    //navigation tabs color states and animations
    var notesColorState by remember { mutableStateOf(Yellow) }
    var tasksColorState by remember { mutableStateOf(Color.Black) }
    val notesColor by animateColorAsState(targetValue = notesColorState, tween(durationMillis = 200))
    val tasksColor by animateColorAsState(targetValue = tasksColorState, tween(durationMillis = 200))
    
    //top navigation tab panel with settings icon
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray10)
            .padding(start = 8.dp, end = 8.dp)
    ) {
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
                            tabState = 0
                            notesColorState = Yellow
                            tasksColorState = Color.Black
                        },
                    style = Typo.h1,
                    color = notesColor
                )
                Text (
                    text = "Tasks",
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            tabState = 1
                            notesColorState = Color.Black
                            tasksColorState = Yellow
                        },
                    style = Typo.h1,
                    color = tasksColor,
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

        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                //search bar
                var searchBarTextState by remember { mutableStateOf("") }
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                    value = searchBarTextState,
                    onValueChange = { newText->
                        searchBarTextState = newText
                    },
                    placeholder = {Text("Search " + if(tabState==0) "notes" else "tasks", color = Gray40)},
                    onSearch = {
                        Toast.makeText(context, "Searching $searchBarTextState...", Toast.LENGTH_SHORT).show()
                        focusManager.clearFocus()
                    }
                )

                StaggeredVerticalGrid {
                    val cards = 20;
                    for(i in 0..cards){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            backgroundColor = Color.White,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = sampleText[Random.nextInt(sampleText.size-1)],
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

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
                        .offset(y = (-15).dp),
                    fontSize = 100.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

//Search bar composable function
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder:  @Composable (() -> Unit)? = null,
    height: Dp = 55.dp,
    onSearch: (KeyboardActionScope.() -> Unit)? = null
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions(onSearch = onSearch),
        placeholder = placeholder,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "",
                tint = Gray40,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Gray20,
            textColor = Color.Black,
            cursorColor = Gray40,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            leadingIconColor = Gray40
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(10.dp))
    )
}

//preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesWindow()
}