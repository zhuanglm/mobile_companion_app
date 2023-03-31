package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.SupportButtonTemplate

@Composable
fun CantFindDeviceButton(
    modifier: Modifier = Modifier,
    onHelpClick: () -> Unit
) {
    SupportButtonTemplate(
        onClick =  onHelpClick ,
        text = "Can't find your eSight?",
        painter = painterResource(id = R.drawable.round_question_mark_24),
        modifier = modifier
    )

}

@Preview
@Composable
fun ButtonPreview() {
    CantFindDeviceButton { Unit }
}