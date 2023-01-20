package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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




@Composable
fun AddDeviceButton(
    onClick: @Composable () -> Unit,
    modifier: Modifier
){
    ElevatedButton(
        onClick = { onClick },
        modifier = modifier,
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(Color.Yellow, Color.Black),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        Icon(Icons.Rounded.Add, contentDescription = "Add something new", modifier.padding(15.dp, 0.dp))
        ButtonText("Connect to eSight Go", modifier = modifier.weight(1f).offset(x= 12.dp))
    }
}

@Preview
@Composable
fun AddDeviceButtonPreview(){
    AddDeviceButton(onClick = { Unit }, modifier = Modifier)
}