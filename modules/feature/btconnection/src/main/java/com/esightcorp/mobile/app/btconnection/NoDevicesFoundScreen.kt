/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesFoundViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun NoDevicesFoundRoute(
    navController: NavController,
    vm: NoDevicesFoundViewModel = hiltViewModel()
) {
    BackStackLogger(navController, TAG)

    val btUiState by vm.uiState.collectAsState()
    when (btUiState.isBtEnabled) {
        false -> NavigateBluetoothDisabled(navController = navController)

        true -> {
            NoDevicesFoundScreen(
                onBackButtonClicked = vm::navigateToNoDevicesConnectedScreen,
                onTryAgainClicked = vm::navigateToSearchingScreen,
                onHelpClicked = vm::navigateToUnableToConnectScreen,
                navController = navController
            )
        }
    }
}

//region Internal implementation
private const val TAG = "NoDevicesFoundScreen"

@Composable
private fun NoDevicesFoundScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: OnNavigationCallback? = null,
    onTryAgainClicked: OnNavigationCallback? = null,
    onHelpClicked: OnNavigationCallback? = null,
    navController: NavController
) {
    Log.i(TAG, "NoDevicesFoundScreen: ")
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackButtonClicked?.invoke(navController) },
        bottomButton = {
            CantFindDeviceButton(modifier, R.string.kUnableToConnectFooterButtonText) {
                onHelpClicked?.invoke(navController)
            }
        },
    ) {
        NoDevicesFoundBody(
            modifier = modifier,
            onTryAgainClicked = { onTryAgainClicked?.invoke(navController) },
        )
    }
}

@Composable
private fun NoDevicesFoundBody(
    modifier: Modifier,
    onTryAgainClicked: OnActionCallback,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subtitle, help1, help2, help3, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.kBTTroubleshootingESightNotFoundTitle),
            modifier = modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 25.dp)
                },
        )

        BoldSubheader(
            text = stringResource(R.string.kTroubleShootingTryFollowingSteps),
            modifier = modifier
                .constrainAs(subtitle) {
                    top.linkTo(header.bottom, margin = 8.dp)
                },
        )

        NumberedHelpItem(
            number = 1,
            text = stringResource(R.string.kTroubleshootingInstructionRestartESightDevice),
            modifier = modifier
                .constrainAs(help1) {
                    top.linkTo(subtitle.bottom, margin = 35.dp)
                },
        )

        NumberedHelpItem(
            number = 2,
            text = stringResource(R.string.kTroubleshootingInstructionESightWithinRange),
            modifier = modifier
                .constrainAs(help2) {
                    top.linkTo(help1.bottom, margin = 35.dp)
                },
        )

        NumberedHelpItem(
            number = 3,
            text = stringResource(R.string.kTroubleshootingInstructionSufficientCharge),
            modifier = modifier
                .constrainAs(help3) {
                    top.linkTo(help2.bottom, margin = 35.dp)
                },
        )

        TextRectangularButton(
            onClick = onTryAgainClicked,
            modifier = modifier
                .constrainAs(button) {
                    top.linkTo(help3.bottom, margin = 35.dp)
                },
            text = stringResource(R.string.kTryAgainButtonTitle)
        )
    }
}

@Preview
@Composable
private fun NoDevicesFoundScreenPreview() = MaterialTheme {
    NoDevicesFoundScreen(
        navController = rememberNavController(),
    )
}

//endregion
