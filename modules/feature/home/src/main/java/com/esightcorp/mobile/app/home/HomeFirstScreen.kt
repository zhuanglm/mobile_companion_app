/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.state.HomeUiState.BluetoothState
import com.esightcorp.mobile.app.home.viewmodels.HomeViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.DeviceCard
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.buttons.IconAndTextSquareButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.containers.HomeBaseScreen
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.utils.findActivity
import com.esightcorp.mobile.app.utils.permission.PermissionUiState

@Composable
fun HomeFirstScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)

    BaseHomeScreen(
        vm = vm,
        navController = navController,
        onSettingsButtonInvoked = vm::navigateToSettings,
        onRemoteDeviceDisconnected = vm::onBleDisconnected,
        onBluetoothDisabled = vm::navigateToBluetoothDisabled,
        onFeedbackButtonPressed = vm::gotoEsightFeedbackSite,
        onNoDeviceConnected = vm::navigateToNoDeviceConnected,
    )
}

@Composable
private fun BaseHomeScreen(
    vm: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onNoDeviceConnected: OnNavigationCallback,
    onSettingsButtonInvoked: OnNavigationCallback,
    onRemoteDeviceDisconnected: OnNavigationCallback,
    onBluetoothDisabled: OnNavigationCallback,
    onFeedbackButtonPressed: OnActionCallback,
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = vm::onPermissionsUpdated
    )
    val context = LocalContext.current

    ExecuteOnce { vm.registerPermissionLauncher(permissionLauncher, context.findActivity()) }

    val homeUiState by vm.uiState.collectAsState()

    Log.w(TAG, "bluetoothState: ${homeUiState.bluetoothState}")
    when (homeUiState.bluetoothState) {
        null -> ExecuteOnce { onNoDeviceConnected(navController) }

        BluetoothState.DISCONNECTED -> ExecuteOnce { onRemoteDeviceDisconnected(navController) }

        BluetoothState.DISABLED -> ExecuteOnce { onBluetoothDisabled(navController) }

        BluetoothState.CONNECTED -> {
            var selectedFeature by rememberSaveable { mutableStateOf<FeatureType?>(null) }

            HomeBaseScreen(
                modifier = modifier,
                showBackButton = false,
                showSettingsButton = true,
                onBackButtonInvoked = { },
                onSettingsButtonInvoked = { onSettingsButtonInvoked.invoke(navController) },
                bottomButton = { FeedbackButton(modifier) { onFeedbackButtonPressed() } },
            ) {
                HomeScreenBody(
                    modifier = modifier,
                    device = homeUiState.connectedDevice,
                    onFeatureClicked = { feature ->
                        selectedFeature = feature
                        Log.i(TAG, "Selected feature: $selectedFeature")
                        vm.initPermissionCheck()
                    },
                )
            }

            DisposableEffect(Unit) { onDispose { vm.cleanUp() } }

            val permissionUiState by vm.permissionUiState.collectAsState()
            Log.i(TAG, "WiFi (Location) permission state: ${permissionUiState.state}")

            when (permissionUiState.state) {
                PermissionUiState.PermissionState.GRANTED -> {
                    val isLocationServiceEnabled by vm.isLocationServiceEnabled.collectAsState()
                    Log.i(TAG, "Location service enabled: $isLocationServiceEnabled")

                    ExecuteOnce(key = isLocationServiceEnabled) {
                        when (isLocationServiceEnabled) {
                            null -> vm.verifyLocationServiceState()

                            true -> when (selectedFeature) {
                                FeatureType.FEATURE_ESHARE -> vm.navigateToShareYourView(
                                    navController
                                )

                                FeatureType.FEATURE_WIFI -> vm.navigateToWifiCredsOverBt(
                                    navController
                                )

                                else -> Unit
                            }

                            false -> vm.navigateToLocationServiceOff(navController)
                        }
                    }
                    return
                }

                PermissionUiState.PermissionState.SHOW_RATIONALE -> {
                    selectedFeature = null
                    ExecuteOnce { vm.navigateToLocationPermission(navController) }
                    return
                }

                null -> Unit
            }
        }

        BluetoothState.ENABLED -> Unit
    }
}

//region Private implementation
private const val TAG = "Home Screen"

@Composable
private fun HomeScreenBody(
    modifier: Modifier = Modifier,
    device: String,
    onFeatureClicked: ((FeatureType) -> Unit),
) {
    val density = LocalDensity.current
    val fixedFontScaleDensity = Density(
        density = density.density,
        fontScale = 1f //ignore the font scaling
    )
    val configuration = LocalConfiguration.current
    val greetingTopMargin = if (configuration.fontScale > 1) {
        (32 / configuration.fontScale).dp
    } else {
        32.dp
    }
    val deviceCardTopMargin = if (configuration.fontScale > 1) {
        (25 / configuration.fontScale).dp
    } else {
        25.dp
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (personalGreeting, deviceCard, appContainer) = createRefs()
        CompositionLocalProvider(LocalDensity provides fixedFontScaleDensity) {
            PersonalGreeting(
                modifier = modifier
                    .semantics {
                        isTraversalGroup = true
                        traversalIndex = 0f
                    }
                    .constrainAs(personalGreeting) {
                        top.linkTo(parent.top, margin = greetingTopMargin)
                        start.linkTo(parent.start)
                    },
                connected = true,
            )
        }
        DeviceCard(
            modifier = modifier
                .semantics {
                    isTraversalGroup = true
                    traversalIndex = 1f
                }
                .constrainAs(deviceCard) {
                    top.linkTo(personalGreeting.bottom, margin = deviceCardTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onClick = { },
            serialNumber = device.substringAfterLast('-'),
        )

        SquareTileCardLayout(
            modifier = modifier
                .semantics {
                    isTraversalGroup = true
                    traversalIndex = 2f
                }
                .constrainAs(appContainer) {
                    top.linkTo(deviceCard.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            onFeatureClicked = onFeatureClicked,
        )
    }
}

private data class CardData(
    @StringRes val labelId: Int,
    @DrawableRes val iconResId: Int,
    val onClick: OnActionCallback,
)

private enum class FeatureType {
    FEATURE_WIFI,
    FEATURE_ESHARE,
}

@Composable
private fun SquareTileCardLayout(
    modifier: Modifier = Modifier,
    onFeatureClicked: ((FeatureType) -> Unit),
) {
    val cards = listOf(
        CardData(
            R.string.kConnectWifiLabelText,
            R.drawable.round_wifi_24,
        ) { onFeatureClicked(FeatureType.FEATURE_WIFI) },

        CardData(
            R.string.kHomeRootViewConnectedeShareButtonText,
            R.drawable.baseline_camera_alt_24,
        ) { onFeatureClicked(FeatureType.FEATURE_ESHARE) },
    )

    val configuration = LocalConfiguration.current
    Log.i(
        TAG,
        "SquareTileCardLayout: with font scale ${((configuration.screenWidthDp - 75) / 2) * configuration.fontScale}"
    )
    val adaptiveCells = (((configuration.screenWidthDp - 75) / 2) * configuration.fontScale).dp

    LazyVerticalGrid(
        columns = GridCells.Adaptive(adaptiveCells),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        itemsIndexed(cards) { _, card ->
            IconAndTextSquareButton(
                text = stringResource(card.labelId),
                painter = painterResource(card.iconResId),
                onClick = card.onClick,
                modifier = modifier.padding(top = 25.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseHomeScreenPreview() {
    HomeScreenBody(
        device = "012345678",
        onFeatureClicked = { },
    )
}
