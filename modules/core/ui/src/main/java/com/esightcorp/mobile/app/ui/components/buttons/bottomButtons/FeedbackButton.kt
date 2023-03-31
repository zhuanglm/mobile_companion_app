package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.SupportButtonTemplate

//TODO: String resources

@Composable
fun FeedbackButton(
    modifier: Modifier = Modifier,
    onFeedbackClick: () -> Unit
) {
    SupportButtonTemplate(
        onClick =  onFeedbackClick ,
        modifier = modifier,
        painter = painterResource(id = R.drawable.round_chat_bubble_outline_24),
        text = "Feedback"
    )
}