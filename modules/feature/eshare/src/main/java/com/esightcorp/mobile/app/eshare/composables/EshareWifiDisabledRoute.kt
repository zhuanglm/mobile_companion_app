/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.SetupHotspotButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.containers.Centered
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.SubHeader
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun EshareWifiDisabledRoute(
    navController: NavController,
    vwModel: EshareViewModel = hiltViewModel(),
) {
    val isDeviceConnected by vwModel.devConnectionState.collectAsState()
    if (!isDeviceConnected) {
        LaunchedEffect(Unit) { vwModel.onBleDisconnected(navController) }
        return
    }

    EshareWifiDisabledScreen(
        navController = navController,
        onRetryPressed = vwModel::onRetryPressed,
        onBackPressed = vwModel::gotoMainScreen,
        onSetupHotspotPressed = vwModel::onSetupHotspotPressed
    )
}

//region Internal implementation

@Composable
internal fun EshareWifiDisabledScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onRetryPressed: OnNavigationCallback? = null,
    onBackPressed: OnNavigationCallback? = null,
    onSetupHotspotPressed: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    showSettingsButton = false,
    isContentCentered = true,
    onBackButtonInvoked = { onBackPressed?.invoke(navController) },
    onSettingsButtonInvoked = { },
    bottomButton = { SetupHotspotButton { onSetupHotspotPressed?.invoke(navController) } },
) {
    Centered {
        BigIcon(
            drawableId = R.drawable.round_wifi_24,
            contentDescription = stringResource(id = R.string.kAccessibilityIconWiFi)
        )

        ItemSpacer(30.dp)
        Header1Text(
            text = stringResource(R.string.kWifiErrorWifiDisabledTitle),
            modifier = modifier,
            textAlign = TextAlign.Center,
        )

        ItemSpacer(30.dp)
        SubHeader(
            text = stringResource(R.string.kWifiErrorWifiDisabledSubtitle),
            modifier = modifier,
            textAlign = TextAlign.Center,
        )

        ItemSpacer(60.dp)
        TextRectangularButton(
            onClick = { onRetryPressed?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kRetryButtonTitle),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
internal fun Preview() = MaterialTheme {
    EshareWifiDisabledScreen(navController = rememberNavController())
}

//endregion
