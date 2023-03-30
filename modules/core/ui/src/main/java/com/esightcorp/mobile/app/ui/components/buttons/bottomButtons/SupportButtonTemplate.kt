package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButtonElevation

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
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
    textColor: Color = MaterialTheme.colors.onSurface
) {
    ExtendedFloatingActionButton(
        text = { BodyText(text = text, modifier = modifier, color = textColor) },
        icon = { SupportButtonIcon(onClick = { }, modifier = modifier, painter = painter) },
        onClick = onClick ,
        containerColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        modifier = modifier.fillMaxWidth(),
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
    )
}


@Composable
private fun SupportButtonIcon(
    onClick:  () -> Unit,
    modifier: Modifier,
    painter: Painter,

    ) {
    FilledIconButton(
        onClick =  onClick ,
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(MaterialTheme.colors.primary, MaterialTheme.colors.onPrimary),
    ) {
        Icon(
            painter = painter,
            contentDescription = "Feedback",
            modifier = Modifier.size(30.dp)
        )
    }
}