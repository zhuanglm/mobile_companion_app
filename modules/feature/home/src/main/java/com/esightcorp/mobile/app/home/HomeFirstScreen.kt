/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.state.HomeUiState
import com.esightcorp.mobile.app.home.viewmodels.HomeViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.DeviceCard
import com.esightcorp.mobile.app.ui.components.buttons.IconAndTextSquareButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.containers.HomeBaseScreen
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

private const val TAG = "Home Screen"

@Composable
fun HomeFirstScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel()
) {
    val homeUiState by vm.uiState.collectAsState()

    BackStackLogger(navController, TAG)

    BaseHomeScreen(
        vm = vm,
        homeUiState = homeUiState,
        navController = navController,
        device = homeUiState.connectedDevice,
        modifier = Modifier,
        onSettingsButtonInvoked = vm::navigateToSettings,
        onRemoteDeviceDisconnected = vm::onBleDisconnected
    )
}

@Composable
private fun BaseHomeScreen(
    vm: HomeViewModel,
    homeUiState: HomeUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
    device: String = "0123456",
    onSettingsButtonInvoked: OnNavigationCallback,
    onRemoteDeviceDisconnected: OnNavigationCallback,
) {
    if (!homeUiState.isBluetoothConnected && homeUiState.isBluetoothEnabled) {
        Log.d(TAG, "BaseHomeScreen: Not connected but are Enabled")
        LaunchedEffect(Unit) {
            onRemoteDeviceDisconnected(navController)
        }
    } else if (!homeUiState.isBluetoothEnabled) {
        LaunchedEffect(Unit) {
            vm.navigateToBluetoothDisabled(navController)
        }
    } else {
        HomeBaseScreen(
            modifier = modifier,
            showBackButton = false,
            showSettingsButton = true,
            onBackButtonInvoked = { },
            onSettingsButtonInvoked = { onSettingsButtonInvoked.invoke(navController) },
            bottomButton = { FeedbackButton(modifier, vm::showFeedbackPage) },
        ) {
            HomeScreenBody(
                modifier = modifier, device = device, navController = navController, vm = vm
            )
        }
    }
}

@Composable
private fun HomeScreenBody(
    modifier: Modifier = Modifier,
    device: String = "0123456",
    navController: NavController,
    vm: HomeViewModel
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (personalGreeting, deviceCard, appContainer) = createRefs()
        PersonalGreeting(
            modifier = modifier.constrainAs(personalGreeting) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start)
            },
            connected = true,
        )
        DeviceCard(
            modifier = modifier.constrainAs(deviceCard) {
                top.linkTo(personalGreeting.bottom, margin = 25.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onClick = { },
            serialNumber = device.substringAfterLast('-'),
        )

        SquareTileCardLayout(
            modifier = modifier.constrainAs(appContainer) {
                top.linkTo(deviceCard.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            navController = navController,
            vm = vm,
        )
    }
}

private data class CardData(
    @StringRes val labelId: Int,
    @DrawableRes val iconResId: Int,
    val onClick: OnActionCallback
)

@Composable
private fun SquareTileCardLayout(
    modifier: Modifier = Modifier,
    vm: HomeViewModel,
    navController: NavController,
) {
    val cards = listOf(
        CardData(R.string.kConnectWifiLabelText, R.drawable.round_wifi_24) {
            vm.navigateToWifiCredsOverBt(navController)
        },

        CardData(
            R.string.kHomeRootViewConnectedeShareButtonText,
            R.drawable.baseline_camera_alt_24
        ) {
            vm.navigateToShareYourView(navController)
        }
    )

    val configuration = LocalConfiguration.current
    val adaptiveCells = (configuration.fontScale * 150).dp

    LazyVerticalGrid(
        columns = GridCells.Adaptive(adaptiveCells),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        itemsIndexed(cards) { _, card ->
            IconAndTextSquareButton(
                text = stringResource(card.labelId),
                painter = painterResource(id = card.iconResId),
                onClick = card.onClick,
                modifier = modifier.padding(top = 25.dp),
            )
        }
    }
}
