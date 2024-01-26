/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.bluetooth.HotspotStatus
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import com.esightcorp.mobile.app.eshare.state.HotspotSetupUiState
import com.esightcorp.mobile.app.eshare.viewmodels.HotspotSetupViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.IconAndTextSquareButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.UnableToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun HotspotSetupRoute(
    navController: NavController,
    vm: HotspotSetupViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    Log.i(TAG, "hotspotStatus: ${uiState.hotspotStatus}, isConnected: ${uiState.isDeviceConnected}")

    if (!uiState.isDeviceConnected) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    when (uiState.hotspotStatus) {
        HotspotStatus.ENABLED -> {
            HotspotSetupScreen(
                navController = navController,
                onBackPressed = vm::gotoMainScreen,
                onShareViewPressed = vm::onRetryPressed,
                onUnableToConnectPressed = vm::showHowToConnectPage,
                modifier = Modifier,
                uiState = uiState
            )
        }

        HotspotStatus.ERROR, HotspotStatus.DISABLED -> {
            LaunchedEffect(Unit) { vm.showHotspotSetupErrorPage(navController) }
        }

        else -> {
            LoadingScreenWithSpinner(
                loadingText = stringResource(R.string.kHotspotViewControllerConnectingSpinnerTitle),
                cancelButtonNeeded = false,
                onCancelButtonClicked = { },
            )
        }
    }
}

//region Internal implementation
private const val TAG = "HotspotSetupRoute"

@Composable
private fun HotspotSetupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBackPressed: OnNavigationCallback? = null,
    onShareViewPressed: OnNavigationCallback? = null,
    onUnableToConnectPressed: OnActionCallback? = null,
    uiState: HotspotSetupUiState
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = { UnableToConnectButton { onUnableToConnectPressed?.invoke() } },
    ) {
        uiState.hotspotCredential?.let {
            HotspotSetupBody(
                modifier = modifier,
                navController = navController,
                onShareViewPressed = onShareViewPressed,
                uiState = uiState
            )
        }
    }
}

@Composable
private fun HotspotSetupBody(
    modifier: Modifier,
    navController: NavController,
    onShareViewPressed: OnNavigationCallback? = null,
    uiState: HotspotSetupUiState
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subtitle, instruction, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.kHotspotTroubleshootingHowToConnectTitle),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top)
            },
        )

        Subheader(
            text = stringResource(R.string.kHotspotTroubleshootingHowToConnectSubTitle),
            modifier = modifier.constrainAs(subtitle) {
                top.linkTo(header.bottom, margin = 10.dp)
            },
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.constrainAs(instruction) {
                top.linkTo(subtitle.bottom, margin = 10.dp)
            }
        ) {
            NumberedHelpItem(
                number = 1,
                text = stringResource(R.string.kHotspotTroubleshootingOpenSetting),
            )
            ItemSpacer()

            NumberedHelpItem(
                number = 2,
                text = stringResource(
                    R.string.kHotspotTroubleshootingConnectToeSight
                ) +" "+uiState.hotspotCredential!!.ssid + " " +stringResource(id = R.string.kHotspotTroubleshootingNetwork),
            )
            ItemSpacer()

            NumberedHelpItem(
                number = 3,
                text = stringResource(
                    R.string.kHotspotTroubleshootingEnterPassword,
                    uiState.hotspotCredential.password
                ),
            )
            ItemSpacer()

            NumberedHelpItem(
                number = 4,
                text = stringResource(R.string.kHotspotTroubleshootingPressBelowButton),
            )
            ItemSpacer()
        }

        val configuration = LocalConfiguration.current
        val adaptiveSize = (configuration.fontScale * 125).dp

        IconAndTextSquareButton(
            text = stringResource(R.string.kHomeRootViewConnectedeShareButtonText),
            painter = painterResource(R.drawable.baseline_camera_alt_24),
            onClick = { onShareViewPressed?.invoke(navController) },
            modifier = modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    top.linkTo(instruction.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(adaptiveSize)
        )
    }
}

@Preview
@Composable
private fun HotspotSetupScreenPreview() = MaterialTheme {
    HotspotSetupScreen(
        navController = rememberNavController(),
        uiState = HotspotSetupUiState(
            hotspotCredential = EshareRepository.HotspotCredential(
                "AAA-12345678-BCDE",
                "12345678"
            )
        ),
    )
}
//endregion
