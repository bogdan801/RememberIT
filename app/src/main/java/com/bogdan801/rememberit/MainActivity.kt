package com.bogdan801.rememberit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
                    //Text(text = "puuuuk")
                }
            }
        }
    }
}

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
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
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
                    .padding(bottom = 16.dp),
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

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
                .background(
                    brush =
                        if(tabState==0) Brush.verticalGradient(0f to Yellow, 1000f to Color.White)
                        else Brush.verticalGradient(0f to Color.White, 1000f to Yellow)
                )
            )

            FloatingActionButton (
                modifier = Modifier
                    .padding(20.dp)
                    .size(70.dp)
                    .align(Alignment.CenterHorizontally),
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
                        .offset(y = (-14).dp),
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