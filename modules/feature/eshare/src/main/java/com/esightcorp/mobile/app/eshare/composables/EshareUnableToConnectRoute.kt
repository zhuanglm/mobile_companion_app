/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.SetupHotspotButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader
import com.esightcorp.mobile.app.ui.components.text.FineText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun EshareUnableToConnectRoute(
    navController: NavController,
    vm: EshareViewModel = hiltViewModel(),
) {
    BackStackLogger(navController = navController, TAG)

    val isDeviceConnected by vm.devConnectionState.collectAsState()
    if (!isDeviceConnected) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    EshareUnableToConnectScreen(
        modifier = Modifier,
        navController = navController,
        onBackPressed = vm::gotoMainScreen,
        onRetryPressed = vm::onRetryPressed,
        onSetupHotspotPressed = vm::onSetupHotspotPressed,
    )
}

//region Internal implementation

private const val TAG = "EshareUnableToConnectRoute"

@Composable
private fun EshareUnableToConnectScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBackPressed: OnNavigationCallback? = null,
    onRetryPressed: OnNavigationCallback? = null,
    onSetupHotspotPressed: OnNavigationCallback? = null,
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = { SetupHotspotButton { onSetupHotspotPressed?.invoke(navController) } },
        bottomAlignedContent = {
            FineText(
                text = stringResource(R.string.kEshareTroubleshootingUnableToConnectDescriptionText),
                modifier = modifier.padding(top = 5.dp, bottom = 5.dp),
                MaterialTheme.colors.onSurface,
            )
        },
    ) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (header, subtitle, instruction, footer) = createRefs()

            Header1Text(
                stringResource(R.string.kTroubleshootingUnableToConnectTitle),
                modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                },
            )

            BoldSubheader(
                stringResource(R.string.kTroubleshootingUnableToConnectSubtitle),
                modifier.constrainAs(subtitle) {
                    top.linkTo(header.bottom, margin = 10.dp)
                },
            )

            Column(
                modifier = modifier
                    .constrainAs(instruction) {
                        top.linkTo(subtitle.bottom, margin = 15.dp)
                        bottom.linkTo(footer.top, margin = 15.dp)
                    }
            ) {
                NumberedHelpItem(
                    number = 1,
                    text = stringResource(R.string.kTroubleshootingInstructionEsightConnectedToWifi)
                )
                ItemSpacer()

                NumberedHelpItem(
                    number = 2,
                    text = stringResource(R.string.kTroubleshootingInstructionEsightWithinRangeShort)
                )
                ItemSpacer()

                NumberedHelpItem(
                    number = 3,
                    text = stringResource(R.string.kTroubleshootingInstructionsSufficientChargeShort)
                )
                ItemSpacer()

                NumberedHelpItem(
                    number = 4,
                    text = stringResource(R.string.kTroubleshootingInstructionEsightIsConnectedToSameWifi)
                )
                ItemSpacer()

                ItemSpacer(20.dp)
                TextRectangularButton(
                    onClick = { onRetryPressed?.invoke(navController) },
                    modifier = modifier,
                    text = stringResource(R.string.kTryAgainButtonTitle),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EshareUnableToConnectScreenPreview() = MaterialTheme {
    EshareUnableToConnectScreen(navController = rememberNavController())
}
//endregion
