package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconAndTextRectangularButton(
    onClick: () -> Unit,
    modifier: Modifier,
    icon: ImageVector,
    iconContextDescription: String? = null,
    text: String,
) {
    ElevatedButton(
        onClick =  onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(25.dp, 20.dp),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colors.primary, MaterialTheme.colors.onPrimary),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        Icon(icon, contentDescription = iconContextDescription)
        ButtonText(text = text, modifier = modifier
            .weight(1f)
            .offset(x = 12.dp))
    }
}

@Composable
fun TextRectangularButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
) {
    ElevatedButton(
        onClick =  onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(25.dp, 20.dp),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(Color.Yellow, Color.Black),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        ButtonText(text = text, modifier = modifier
            .weight(1f)
            .offset(x = 12.dp))
    }
}

@Preview
@Composable
fun IconAndTextRectangularButtonPreview() {
    IconAndTextRectangularButton(
        onClick = { },
        modifier = Modifier,
        icon = Icons.Default.Star,
        text = "Preview"
    )
}