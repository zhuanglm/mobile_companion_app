package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.SupportButtonTemplate
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

@Composable
fun FeedbackButton(
    @Suppress("UNUSED_PARAMETER") modifier: Modifier = Modifier,
    onFeedbackClick: OnActionCallback
) {
    SupportButtonTemplate(
        onClick = onFeedbackClick,
        painter = painterResource(id = R.drawable.round_chat_bubble_outline_24),
        text = stringResource(id = R.string.kFeedbackButtonText)
    )
}