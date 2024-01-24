/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.esightcorp.mobile.app.eshare.viewmodels.EshareWifiSetupViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.IconAndTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.SetupHotspotButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun EshareSetupWifiRoute(
    navController: NavController,
    vm: EshareWifiSetupViewModel = hiltViewModel()
) {
    val isDeviceConnected by vm.devConnectionState.collectAsState()
    if (!isDeviceConnected) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    EshareSetupWifiScreen(
        navController = navController,
        onBackPressed = vm::gotoMainScreen,
        onSetupWifiPressed = vm::gotoWifiSetup,
        onSetupHotspotPressed = vm::onSetupHotspotPressed,
    )
}

//region Internal implementation

@Composable
private fun EshareSetupWifiScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackPressed: OnNavigationCallback? = null,
    onSetupWifiPressed: OnNavigationCallback? = null,
    onSetupHotspotPressed: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    showSettingsButton = false,
    onBackButtonInvoked = { onBackPressed?.invoke(navController) },
    onSettingsButtonInvoked = { },
    bottomButton = { SetupHotspotButton { onSetupHotspotPressed?.invoke(navController) } },
    bottomAlignedContent = {
        BodyText(
            text = stringResource(R.string.kEshareViewControllerWifiNotConnectedFooterText),
            modifier,
            MaterialTheme.colors.onSurface,
        )
    }
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subHeader, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.kConnectWifiLabelText),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top)
            }
        )

        Subheader(
            text = stringResource(R.string.kEShareViewControllerWifiNotConnectedSubHeaderText),
            modifier = modifier.constrainAs(subHeader) {
                top.linkTo(header.bottom, margin = 20.dp)
            }
        )

        IconAndTextRectangularButton(
            onClick = { onSetupWifiPressed?.invoke(navController) },
            modifier = modifier.constrainAs(button) {
                top.linkTo(subHeader.bottom, margin = 50.dp)
            },
            text = stringResource(R.string.kConnectWifiLabelText),
            iconDrawableId = R.drawable.round_wifi_24,
        )
    }
}

@Preview
@Composable
private fun EshareSetupWifiScreenPreview() = MaterialTheme {
    EshareSetupWifiScreen(navController = rememberNavController())
}
//endregion
