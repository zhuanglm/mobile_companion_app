/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

@Composable
fun UnableToConnectButton(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.kFooterButtonStillUnableToConnectTitle,
    onClick: OnActionCallback
) {
    SupportButtonTemplate(
        onClick =  onClick ,
        text = stringResource(labelId),
        description = stringResource(id = R.string.kFooterButtonStillUnableToConnectTitle),
        painter = painterResource(id = R.drawable.round_question_mark_24),
        modifier = modifier
    )

}

@Preview
@Composable
fun UnableToConnectPreview() {
    UnableToConnectButton { }
}