package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

@Composable
fun FeedbackButton(
    modifier: Modifier = Modifier,
    onFeedbackClick: OnActionCallback
) {
    SupportButtonTemplate(
        onClick = onFeedbackClick,
        modifier = modifier.defaultMinSize(
            minHeight = dimensionResource(id = R.dimen.min_dimension_touch_area),
            minWidth = dimensionResource(id = R.dimen.min_dimension_touch_area)
        ),
        painter = painterResource(id = R.drawable.round_chat_bubble_outline_24),
        text = stringResource(id = R.string.kFeedbackButtonText)
    )
}