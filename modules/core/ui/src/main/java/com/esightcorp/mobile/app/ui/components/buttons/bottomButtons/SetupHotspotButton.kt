package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.SupportButtonTemplate

@Composable
fun SetupHotspotButton(
    modifier: Modifier = Modifier,
    onSetupHotspotClicked: () -> Unit,
) {
    //TODO: Icon rendering is all black for some reason, probably inherited properties
    SupportButtonTemplate(
        modifier = modifier,
        onClick = onSetupHotspotClicked,
        text = "Set up a hotspot",
        painter = painterResource(id = R.drawable.round_question_mark_24)
    )
}