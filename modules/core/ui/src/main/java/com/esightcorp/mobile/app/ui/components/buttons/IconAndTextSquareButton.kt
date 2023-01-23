package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconAndTextSquareButton(
    onClick: @Composable () -> Unit,
    modifier: Modifier,
    icon: ImageVector,
    iconContextDescription: String? = null,
    text: String,
) {
    ElevatedButton(
        onClick = { onClick },
        modifier = modifier
            .padding(25.dp, 20.dp),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(Color.Yellow, Color.Black),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconContextDescription,
                modifier = modifier.defaultMinSize(85.dp, 85.dp)
            )
            ButtonText(text = text, modifier = modifier)
        }

    }
}

@Preview
@Composable
fun IconAndTextSquareButtonPreview() {
    IconAndTextSquareButton(
        onClick = { },
        modifier = Modifier,
        icon = Icons.Default.Star,
        text = "Connect to Wifi"
    )
}