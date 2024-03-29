/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.text

import androidx.compose.material.*
import androidx.compose.material3.Shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.esightcorp.mobile.app.ui.R


@Composable
fun PasswordEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    description: String = "",
    label: @Composable (() -> Unit)? = null,
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }


    OutlinedTextField(value = value,
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
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                if (passwordVisibility) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = stringResource(id = R.string.kAccessibilityIconPasswordVisible)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                        contentDescription = stringResource(id = R.string.kAccessibilityIconPasswordNotVisible)
                    )
                }

            }
        })
}
