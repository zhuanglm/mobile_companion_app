package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//TODO: Update the content descriptions to use resources
//TODO: Change all colours to follow material theme
//TODO: Extrapolate values to res file
//TODO: Create previews for everything

/**
 * Displays a button styled as an `ElevatedButton` with a text "Connect to eSight".
 * This button spans the entire width of its parent and has rounded corners.
 *
 * This composable is primarily used to initiate the connection process to an eSight device.
 * The button color scheme uses the primary color for the background and the onPrimary color for the text.
 *
 * @param onClick A lambda function that will be triggered when the button is clicked.
 * @param modifier A [Modifier] for applying layout and styling to this composable.
 *
 * @see ElevatedButton
 * @sample AddDeviceButtonPreview
 */
@Composable
fun AddDeviceButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colors.primary, MaterialTheme.colors.onPrimary),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        ButtonText("Connect to eSight", modifier = modifier.weight(1f))
    }
}

@Preview
@Composable
fun AddDeviceButtonPreview() {
    AddDeviceButton(onClick = { }, modifier = Modifier)
}