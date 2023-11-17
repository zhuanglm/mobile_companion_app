package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.SupportButtonTemplate

@Composable
fun CantFindDeviceButton(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.bt_label_unable_to_find_help,
    onHelpClick: () -> Unit
) {
    SupportButtonTemplate(
        onClick = onHelpClick,
        text = stringResource(labelId),
        painter = painterResource(id = R.drawable.round_question_mark_24),
        modifier = modifier
    )
}

@Preview
@Composable
fun ButtonPreview() {
    CantFindDeviceButton { }
}
