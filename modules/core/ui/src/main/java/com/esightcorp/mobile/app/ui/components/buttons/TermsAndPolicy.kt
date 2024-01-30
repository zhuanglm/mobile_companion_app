/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.ClickableBodyText

@Composable
fun TermsAndPolicy(
    onTermsInvoked: (Int) -> Unit,
    onPrivacyPolicyInvoked: (Int) -> Unit,
    modifier: Modifier,
    textColor: Color
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ClickableBodyText(
            text = "${stringResource(R.string.kTermsOfServiceAgreementPartOne)} ${stringResource(R.string.kTermsOfService)}",
            modifier = modifier.fillMaxWidth(),
            onClick = onTermsInvoked,
            color = textColor
        )
        ClickableBodyText(
            text = "${stringResource(R.string.kTermsOfServiceAgreementPartTwo)} ${stringResource(R.string.kDataPolicy)}",
            modifier = modifier.fillMaxWidth(),
            onClick = onPrivacyPolicyInvoked,
            color = textColor
        )
    }
}

@Preview(locale = "es")
@Composable
private fun TermsAndPolicyPreview() = MaterialTheme {
    TermsAndPolicy(
        onTermsInvoked = {},
        onPrivacyPolicyInvoked = {},
        modifier = Modifier,
        textColor = MaterialTheme.colors.primary,
    )
}
