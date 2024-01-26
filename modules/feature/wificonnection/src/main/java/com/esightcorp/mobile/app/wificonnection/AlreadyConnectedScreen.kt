/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.IconAndTextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation

@Composable
fun AlreadyConnectedRoute(
    navController: NavController,
) {
    AlreadyConnectedScreen(
        navController = navController, modifier = Modifier
    )
}

@Preview
@Composable
private fun AlreadyConnectedScreenPreview() = MaterialTheme {
    AlreadyConnectedScreen(
        navController = rememberNavController(),
        modifier = Modifier,
        onBackPressed = {},
    )
}

@Composable
internal fun AlreadyConnectedScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {
        navController.navigate(
            target = HomeNavigation.FirstScreenRoute,
            popUntil = WifiNavigation.IncomingRoute
        )
    },
) {

    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = onBackPressed,
        isBottomButtonNeeded = false,
        bottomButton = {},
    ) {
        AlreadyConnectedScreenBody(modifier = modifier, navController = navController)
    }
}

@Composable
private fun AlreadyConnectedScreenBody(
    modifier: Modifier,
    navController: NavController
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (headerText, header2Text, helpText, button) = createRefs()

        Header1Text(
            text = stringResource(id = R.string.kWifiConnectedTitle),
            modifier = modifier.constrainAs(headerText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
        )

        BodyText(
            text = stringResource(id = R.string.kWifiAlreadyConnectedSubtitle),
            modifier = modifier.constrainAs(header2Text) {
                top.linkTo(headerText.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            },
            color = MaterialTheme.colors.onSurface
        )

        Subheader(
            text = stringResource(id = R.string.kWifiAlreadyConnectedDescription),
            modifier = modifier.constrainAs(helpText) {
                top.linkTo(header2Text.bottom, margin = 20.dp)
                start.linkTo(parent.start)
            },
        )

        IconAndTextRectangularButton(
            onClick = {
                navController.navigate(
                    target = WifiNavigation.ScanningRoute,
                    param = WifiNavigation.ScanningRoute.PARAM_BLUETOOTH,
                )
            },
            modifier = modifier.constrainAs(button) {
                top.linkTo(helpText.bottom, margin = 20.dp)
            },
            icon = ImageVector.vectorResource(id = R.drawable.round_wifi_24),
            text = stringResource(id = R.string.kConnectWifiLabelText)
        )
    }
}