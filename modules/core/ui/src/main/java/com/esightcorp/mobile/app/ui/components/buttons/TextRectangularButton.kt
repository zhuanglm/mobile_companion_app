package com.esightcorp.mobile.app.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.components.text.ButtonText

@Composable
fun TextRectangularButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.elevatedButtonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary,
            disabledContainerColor = MaterialTheme.colors.primaryVariant,
            disabledContentColor = MaterialTheme.colors.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        ButtonText(
            text = text, modifier = modifier
                .weight(1f)
        )
    }
}
