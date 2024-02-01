/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.ButtonText
import com.esightcorp.mobile.app.ui.components.text.WrappableButton2Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    serialNumber: String = "31415962",
    containerColor: Color = MaterialTheme.colors.secondary,
    borderColor: Color = MaterialTheme.colors.primary,
    modifier: Modifier,
    isClickable: Boolean = false,
    onClick: () -> Unit
) {
    val borderStroke = BorderStroke(6.dp, borderColor)
    val readLabel = stringResource(R.string.kBTPairingHeader)

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp), //TODO: need to swap this to take up width
        colors = CardDefaults.outlinedCardColors(containerColor),
        enabled = true,
        border = borderStroke,
        shape = RoundedCornerShape(18.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImageForDeviceCard(Modifier.weight(1F))
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier
                    .semantics(mergeDescendants = true) {
                        //avoid talkBack read it as "double click to active"
                        contentDescription = if (isClickable) readLabel else ""
                    }
                    .weight(1F), horizontalAlignment = Alignment.Start
            ) {
                ButtonText(text = stringResource(R.string.ego_model_name),
                    modifier = Modifier.clearAndSetSemantics {  })
                WrappableButton2Text(
                    text = "${stringResource(R.string.kBTDeviceFoundeSightGoSN)} $serialNumber",
                    modifier = Modifier.clearAndSetSemantics {  },
                    textAlign = TextAlign.Start
                )
            }


        }
    }
}

@Composable
fun YellowDeviceCard(
    modifier: Modifier = Modifier,
    serialNumber: String = "####$$$$",
    onClick: () -> Unit
) {
    DeviceCard(
        serialNumber = serialNumber,
        containerColor = MaterialTheme.colors.primary,
        modifier = modifier,
        isClickable = true,
        onClick = onClick
    )
}

@Composable
fun ImageForDeviceCard(
    modifier: Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.glasses),
        contentDescription = "",
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}

@Preview
@Composable
fun ConnectedDeviceCardPreview() {

}