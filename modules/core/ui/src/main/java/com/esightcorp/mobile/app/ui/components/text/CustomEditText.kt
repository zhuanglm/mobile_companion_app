package com.esightcorp.mobile.app.ui.components.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CustomEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        shape = Shapes().medium,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondary,
            textColor = MaterialTheme.colors.onSecondary,
            cursorColor = MaterialTheme.colors.onSecondary,
            focusedIndicatorColor = MaterialTheme.colors.primary,
            trailingIconColor = MaterialTheme.colors.onSecondary,
            unfocusedIndicatorColor = MaterialTheme.colors.onSecondary
        )
    )



}
