package com.esightcorp.mobile.app.ui.components.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.components.ButtonText
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.WrappableButtonText


@Composable
fun HelpItemNumber(
    number: Int = 1,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    color: Color = Color.Black
) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .padding(8.dp),
        shape = CircleShape,
        color = Color.Yellow,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Header1Text(
                text = number.toString(),
                color = color,
                modifier = Modifier,)
        }
    }
}

@Composable
fun HelpItemText(
    modifier: Modifier = Modifier,
    text: String = "This is a test",
    color: Color = Color.White,
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
    textColor: Color = Color.White,
    numberColor: Color = Color.Black
) {
    Row(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
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





