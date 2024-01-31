/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

@Composable
fun FeedbackButton(
    @Suppress("UNUSED_PARAMETER") modifier: Modifier = Modifier,
    onFeedbackClick: OnActionCallback
) {
    SupportButtonTemplate(
        onClick = onFeedbackClick,
        painter = painterResource(id = R.drawable.round_chat_bubble_outline_24),
        text = stringResource(id = R.string.kFeedbackButtonText),
        description = stringResource(id = R.string.kFeedbackButtonText)
    )
}