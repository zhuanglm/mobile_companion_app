/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.WrappableButton2Text
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback


@Composable
fun IconAndTextSquareButton(
    onClick: OnActionCallback,
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = R.drawable.glasses),
    iconContextDescription: String? = null,
    text: String = "Defaults",
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colors.primary, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = iconContextDescription,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
            )
            WrappableButton2Text(text = text, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview
@Composable
private fun IconAndTextSquareButtonPreview() {
    IconAndTextSquareButton(
        onClick = { },
        modifier = Modifier,
        painter = painterResource(R.drawable.glasses),
        text = "Connect to Wifi"
    )
}