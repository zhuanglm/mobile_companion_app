/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.ButtonText
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

@Composable
fun CancelButton(
    onClick: OnActionCallback? = null,
    modifier: Modifier,
    text: String = stringResource(R.string.kCancel),
) {
    ElevatedButton(
        onClick = { onClick?.invoke() },
        modifier = modifier.wrapContentSize(),
        enabled = true,
        colors = ButtonDefaults.elevatedButtonColors(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(42.dp, 14.dp),
    ) {
        ButtonText(text = text, modifier = modifier)
    }
}

@Preview
@Composable
fun CancelButtonPreview() {
    CancelButton(modifier = Modifier)
}
