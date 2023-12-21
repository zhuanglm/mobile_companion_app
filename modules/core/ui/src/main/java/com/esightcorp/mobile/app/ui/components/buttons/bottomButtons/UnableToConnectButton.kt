package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.ui.R

@Composable
fun UnableToConnectButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SupportButtonTemplate(
        onClick =  onClick ,
        text = stringResource(id = R.string.kFooterButtonStillUnableToConnectTitle),
        painter = painterResource(id = R.drawable.round_question_mark_24),
        modifier = modifier
    )

}

@Preview
@Composable
fun UnableToConnectPreview() {
    UnableToConnectButton { Unit }
}