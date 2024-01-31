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

//TODO: Icon rendering is all black for some reason, probably inherited properties
@Composable
fun SetupHotspotButton(
    modifier: Modifier = Modifier,
    onClick: OnActionCallback,
) = SupportButtonTemplate(
    modifier = modifier,
    onClick = onClick,
    text = stringResource(R.string.kEshareTroubleshootingUnableToConnectFooterButtonText),
    description = stringResource(R.string.kEshareTroubleshootingUnableToConnectFooterButtonText),
    painter = painterResource(R.drawable.round_question_mark_24)
)
