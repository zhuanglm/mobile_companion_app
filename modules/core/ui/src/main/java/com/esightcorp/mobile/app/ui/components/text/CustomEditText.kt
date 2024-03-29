/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun CustomEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    description: String = "",
    label: @Composable (() -> Unit)? = null,
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.semantics { contentDescription = description },
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
