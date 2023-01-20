package com.esightcorp.mobile.app.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.esightcorp.mobile.app.ui.R


//TODO: Update the content descriptions to use resources
//TODO: Change all colours to follow material theme
//TODO: Extrapolate values to res file
//TODO: Create previews for everything


@Composable
fun FeedbackButton(
    onClick: @Composable () -> Unit
){
    ExtendedFloatingActionButton(
        text = { Text(text = "Feedback") },
        icon =  { FeedbackIcon(onClick = { Unit }, modifier = Modifier) },
        onClick = { onClick },
        containerColor = Color.Black,
        contentColor = Color.White)
}

@Composable
private fun FeedbackIcon(
    onClick: @Composable () -> Unit,
    modifier: Modifier
){
    FilledIconButton(
        onClick = { onClick },
        modifier = modifier,
        enabled = true,
        shape = IconButtonDefaults.filledShape,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow, Color.Black),
    ) {
        Icon(painter = painterResource(id = R.drawable.round_feedback_20), contentDescription = "Feedback")
    }
}