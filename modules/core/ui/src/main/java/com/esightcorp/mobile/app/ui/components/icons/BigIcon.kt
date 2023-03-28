package com.esightcorp.mobile.app.ui.components.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R

@Composable
fun BigIcon(
    painter: Painter,
    contentDescription: String,
    modifier: Modifier
){
    Surface(
        modifier = modifier.size(120.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.primary) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Icon(painter = painter, contentDescription = contentDescription, modifier = modifier.size(75.dp), tint = MaterialTheme.colors.onPrimary)
        }

    }
}

@Preview
@Composable
fun BigIconPreview(){
    BigIcon(painter = painterResource(id = R.drawable.baseline_bluetooth_24), contentDescription = "Bluetooth Icon", modifier = Modifier)
}