package com.esightcorp.mobile.app.ui.components.text

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.esightcorp.mobile.app.ui.R


@Composable
fun PasswordEditText(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier, label: @Composable (() -> Unit)? = null,
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }


    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        shape = Shapes().medium,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White, textColor = Color.Black, cursorColor = Color.Black, focusedIndicatorColor = Color.White, trailingIconColor = Color.Black, unfocusedIndicatorColor = Color.Black),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                if(passwordVisibility){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = "Password visibility toggle - On"
                    )
                }else{
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                        contentDescription = "Password visibility toggle - Off"
                    )
                }

            }
        })
}
