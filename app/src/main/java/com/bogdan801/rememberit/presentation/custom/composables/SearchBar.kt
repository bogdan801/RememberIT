package com.bogdan801.rememberit.presentation.custom.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Пошукове поле
 */
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
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.onBackground,
            textColor = MaterialTheme.colors.onPrimary,
            cursorColor = MaterialTheme.colors.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            leadingIconColor = MaterialTheme.colors.onSurface
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(10.dp))
    )
}
