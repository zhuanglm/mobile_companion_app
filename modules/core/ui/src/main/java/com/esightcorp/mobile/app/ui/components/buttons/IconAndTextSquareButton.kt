package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R

@Composable
fun IconAndTextSquareButton(
    onClick: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = R.drawable.glasses),
    iconContextDescription: String? = null,
    text: String = "Defaults",
) {
    ElevatedButton(
        onClick = { onClick },
        modifier = modifier
            .padding(25.dp, 20.dp).defaultMinSize(100.dp, 100.dp) ,
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(Color.Yellow, Color.Black),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(5.dp, 5.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(0.dp, 0.dp).wrapContentHeight().wrapContentWidth()
        ) {
            Icon(
                painter = painter,
                contentDescription = iconContextDescription,
                modifier = Modifier.defaultMinSize(75.dp, 75.dp)
            )
            WrappableButton2Text(text = text, modifier = Modifier)
        }

    }
}

@Preview
@Composable
fun IconAndTextSquareButtonPreview() {
    IconAndTextSquareButton(
        onClick = { },
        modifier = Modifier,
        painter = painterResource(R.drawable.glasses),
        text = "Connect to Wifi"
    )
}