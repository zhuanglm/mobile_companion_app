package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R


//TODO: Update the content descriptions to use resources
//TODO: Change all colours to follow material theme
//TODO: Extrapolate values to res file
//TODO: Create previews for everything


@Composable
fun SupportButtonTemplate(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = R.drawable.glasses),
    text: String = "Override me",
) {
    ExtendedFloatingActionButton(
        text = { BodyText(text = text, modifier = modifier) },
        icon = { SupportButtonIcon(onClick = { }, modifier = modifier, painter = painter) },
        onClick = { onClick },
        containerColor = Color.Black,
        contentColor = Color.White,
        modifier = modifier
    )
}


@Composable
private fun SupportButtonIcon(
    onClick: @Composable () -> Unit,
    modifier: Modifier,
    painter: Painter,

    ) {
    FilledIconButton(
        onClick = { onClick },
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
    ) {
        Icon(
            painter = painter,
            contentDescription = "Feedback",
            modifier = Modifier.size(30.dp)
        )
    }
}