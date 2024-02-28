/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.UnableToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.extensions.narratorSkip
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation

@Composable
fun HowToScanRoute(
    navController: NavController
) {
    HowToScanScreen(
        onBackClicked = { navController.popBackStack() },
        onUnableToConnectClicked = {
            navController.navigate(
                target = WifiNavigation.UnableToConnectRoute,
                popCurrent = false,
            )
        },
    )
}

//region Private implementation
@Composable
private fun HowToScanScreen(
    modifier: Modifier = Modifier,
    onBackClicked: OnActionCallback? = null,
    onUnableToConnectClicked: OnActionCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    onBackButtonInvoked = onBackClicked,
    showSettingsButton = false,
    bottomButton = {
        UnableToConnectButton(
            labelId = R.string.kUnableToConnectFooterButtonText,
            onClick = { onUnableToConnectClicked?.invoke() }
        )
    },
) {
    Column {
        Header1Text(
            text = stringResource(R.string.kWifiTroubleshootingHowToScanTitle),
            modifier = modifier.padding(bottom = 30.dp)
        )

        NumberedHelpItem(
            number = 1,
            text = stringResource(R.string.kWifiTroubleshootingHowToScanFirstText),
            modifier = modifier.padding(bottom = 20.dp)
        )

        NumberedHelpItem(
            number = 2,
            text = stringResource(R.string.kWifiTroubleshootingHowToScanSecondText),
            semanticsContent = stringResource(R.string.kWifiTroubleshootingHowToScanSecondText)
                .narratorSkip(">"),
            modifier = modifier.padding(bottom = 20.dp)
        )

        NumberedHelpItem(
            number = 3,
            text = stringResource(R.string.kWifiTroubleshootingHowToScanThirdText),
            modifier = modifier.padding(bottom = 20.dp)
        )

        NumberedHelpItem(
            number = 4,
            text = stringResource(R.string.kWifiTroubleshootingHowToScanFourthText),
            modifier = modifier.padding(bottom = 20.dp)
        )
    }
}

@Preview(locale = "fr")
@Composable
private fun HowToScanScreenPreview() = MaterialTheme {
    HowToScanScreen()
}
//endregion
