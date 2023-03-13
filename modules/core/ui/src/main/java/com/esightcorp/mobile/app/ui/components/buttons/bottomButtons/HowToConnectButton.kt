package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.SupportButtonTemplate

@Composable
fun HowToConnectButton(
    modifier: Modifier = Modifier,
    onConnectClick: () -> Unit,
){
    SupportButtonTemplate(
        onClick = onConnectClick,
        text = stringResource(id = R.string.wifi_unable_to_connect_footer_button),
        painter = painterResource(
            id = R.drawable.round_question_mark_24
        ),
        modifier = modifier
    )
}