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

@Composable
fun AdvancedSettingsButton(
    modifier: Modifier = Modifier,
    onAdvancedSettingsClick: () -> Unit,
) {
    //TODO: Icon rendering is all black for some reason, probably inherited properties
    SupportButtonTemplate(
        modifier = modifier,
        onClick = onAdvancedSettingsClick,
        text = stringResource(R.string.kAdvancedSettingsButtonText),
        description = stringResource(id = R.string.kAdvancedSettingsButtonText),
        painter = painterResource(id = R.drawable.blackgear_yellowbackground)

    )
}