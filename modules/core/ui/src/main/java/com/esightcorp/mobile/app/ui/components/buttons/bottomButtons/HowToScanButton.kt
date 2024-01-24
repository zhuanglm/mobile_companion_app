/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R

@Composable
fun HowToScanButton(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit,
){
    SupportButtonTemplate(
        onClick = onScanClick,
        text = stringResource(id = R.string.kQRViewHowToScanButtonText),
        painter = painterResource(
            id = R.drawable.round_question_mark_24
        ),
        modifier = modifier
    )
}