/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.Button2Text
import com.esightcorp.mobile.app.ui.components.text.ButtonText
import com.esightcorp.mobile.app.ui.components.text.WrappableButton2Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    deviceModel: String = stringResource(R.string.ego_model_name),
    serialNumber: String = "31415962",
    containerColor: Color = MaterialTheme.colors.secondary,
    borderColor: Color = MaterialTheme.colors.primary,
    border: Boolean = true,
    modifier: Modifier,
    onClick : () -> Unit
) {
    val borderStroke = BorderStroke(6.dp, borderColor)
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
                    .weight(1F), horizontalAlignment = Alignment.Start
            ) {
                ButtonText(text = stringResource(R.string.ego_model_name), modifier = Modifier)
                WrappableButton2Text(text = "S/N: $serialNumber", modifier = Modifier, textAlign = TextAlign.Start)
            }


        }
    }
}

@Composable
fun YellowDeviceCard(
    deviceModel: String = stringResource(R.string.ego_model_name),
    serialNumber: String = "####$$$$",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    DeviceCard(
        deviceModel = stringResource(R.string.ego_model_name),
        serialNumber = serialNumber,
        border = false,
        containerColor = MaterialTheme.colors.primary,
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
fun ImageForDeviceCard(
    modifier: Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.glasses),
        contentDescription = "Image of the eSight Go",
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight())
}

@Preview
@Composable
fun ConnectedDeviceCardPreview() {

}