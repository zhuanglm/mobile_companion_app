package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    deviceModel: String = "eSight Go",
    serialNumber: String = "31415962",
    containerColor: Color = Color.White,
    borderColor: Color = Color.Yellow,
    border: Boolean = true,
    modifier: Modifier
) {
    val borderStroke = BorderStroke(6.dp, borderColor)
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp), //TODO: need to swap this to take up width
        colors = CardDefaults.outlinedCardColors(containerColor),
        enabled = border,
        border = borderStroke,
        shape = RoundedCornerShape(18.dp),
        onClick = { },
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageForDeviceCard(Modifier.weight(0.7F))
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1F), horizontalAlignment = Alignment.Start
            ) {
                ButtonText(text = deviceModel, modifier = Modifier)
                Button2Text(text = "S/N: $serialNumber", modifier = Modifier)
            }


        }
    }
}

@Composable
fun YellowDeviceCard(
    deviceModel: String = "eSight Go",
    serialNumber: String = "####$$$$",
    modifier: Modifier
) {
    DeviceCard(
        deviceModel = deviceModel,
        serialNumber = serialNumber,
        border = false,
        containerColor = Color.Yellow,
        modifier = modifier
    )
}

@Composable
fun ImageForDeviceCard(
    modifier: Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.glasses),
        contentDescription = "Temporary image for the eSight Go",
        modifier = modifier.size(76.dp)
    )
}

@Preview
@Composable
fun ConnectedDeviceCardPreview() {
//ImageForDeviceCard()
//    DeviceCard()
//    YellowDeviceCard()
}