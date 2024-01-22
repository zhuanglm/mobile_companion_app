package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R

@Composable
fun CantFindWifiButton(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.kWifiTroubleshootingNoWifiFooterButtonTitle,
    onHelpClick: () -> Unit
) {
    SupportButtonTemplate(
        onClick = onHelpClick,
        text = stringResource(labelId),
        painter = painterResource(id = R.drawable.round_question_mark_24),
        modifier = modifier
    )
}
