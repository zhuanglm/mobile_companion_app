package com.esightcorp.mobile.app.ui.components.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.WrappableButtonText


@Composable
fun HelpItemNumber(
    modifier: Modifier = Modifier,
    number: Int = 1,
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Surface(
        modifier = Modifier
            .size(60.dp)
            .padding(8.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.primary,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Header1Text(
                text = number.toString(),
                color = color,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun HelpItemText(
    modifier: Modifier = Modifier,
    text: String = "This is a test",
    color: Color = MaterialTheme.colors.secondary,
) {
    WrappableButtonText(
        text = text,
        modifier = Modifier,
        color = color
    )
}

@Preview
@Composable
fun NumberedHelpItem(
    modifier: Modifier = Modifier,
    number: Int = 1,
    text: String = "This is a test",
    textColor: Color = MaterialTheme.colors.onSurface,
    numberColor: Color = MaterialTheme.colors.onPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HelpItemNumber(
            number = number,
            modifier = Modifier,
            shape = CircleShape,
            color = numberColor,
        )
        HelpItemText(
            modifier = Modifier,
            text = text,
            color = textColor,
        )
    }
}





