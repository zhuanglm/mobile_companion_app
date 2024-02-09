/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.state.EshareStoppedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectionStoppedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader
import com.esightcorp.mobile.app.ui.components.text.SubHeader
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback


@Composable
fun EshareConnectionStoppedRoute(
    navController: NavController,
    reason: EShareStoppedReason? = null,
    vm: EshareConnectionStoppedViewModel = hiltViewModel(),
) {
    vm.updateState(reason)
    val uiState by vm.uiState.collectAsState()

    if (!uiState.isDeviceConnected) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    EshareConnectionStoppedScreen(
        uiState = uiState,
        navController = navController,
        onReturnPressed = vm::gotoMainScreen,
    )
}

//region Internal implementation
private const val TAG = "EshareConnectionStoppedRoute"

@Composable
internal fun EshareConnectionStoppedScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: EshareStoppedUiState,
    onReturnPressed: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    onBackButtonInvoked = { },
    onSettingsButtonInvoked = { },
    bottomButton = { },
) {
    BackStackLogger(navController, TAG)
    val spacerSizingBetweenText = 25.dp
    val spacerSizingBottomButton = 50.dp

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightDp = (configuration.screenHeightDp * 0.8).dp
    val minHeightPx = with(density) {screenHeightDp.toPx()}

    Log.i(TAG, "EshareConnectionStoppedScreen: with density  ${minHeightPx.dp}")
    Log.i(TAG, "EshareConnectionStoppedScreen: Without density $screenHeightDp")


    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(screenHeightDp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BigIcon(drawableId = R.drawable.ic_settings_disconnected,
            contentDescription = stringResource(id = R.string.kAccessibilityIconDisconnected))

        ItemSpacer(spacerSizingBetweenText)
        Header1Text(
            text = stringResource(uiState.titleId),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(spacerSizingBetweenText)
        SubHeader(
            text = stringResource(uiState.descriptionId),
            modifier = modifier,
            textAlign = TextAlign.Center
        )
        Log.i(TAG, "EshareConnectionStoppedScreen: ${uiState.descriptionTwoId}")
        if(uiState.descriptionTwoId !=  -1){
            ItemSpacer(spacerSizingBetweenText)
            SubHeader(
                text = stringResource(id = uiState.descriptionTwoId),
                modifier = modifier,
                textAlign = TextAlign.Center
            )
        }

        ItemSpacer(spacerSizingBottomButton)
        TextRectangularButton(
            onClick = { onReturnPressed?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kEshareErrorViewControllerButtonTitle),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
internal fun ConnectionStoppedPreview() = MaterialTheme {
    EshareConnectionStoppedScreen(
        navController = rememberNavController(),
        uiState = EshareStoppedUiState(
            titleId = R.string.kEshareErrorViewControllerConnectionUnsuccessfulTitle,
            descriptionId = R.string.kHotspotUnreachable,
            descriptionTwoId = R.string.kEshareErrorViewControllerTryAgainLater
        ),
    )
}
//endregion
