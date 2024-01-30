/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionStatus.CONNECTED
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionStatus.DISCONNECTED
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectionStatus.UNKNOWN
import com.esightcorp.mobile.app.wificonnection.state.WifiSearchingUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiSearchingViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchingForNetworksRoute(
    navController: NavController,
    flow: String?,
    vm: WifiSearchingViewModel = hiltViewModel()
) {
    BackStackLogger(navController)

    val uiState by vm.uiState.collectAsState()
    Log.d(TAG, "SearchingForNetworksRoute: {$flow}")
    if (flow != null) {
        LaunchedEffect(Unit) {
            vm.setWifiFlow(flow)
        }
    }
    if (!uiState.isWifiEnabled) {
        NavigateToWifiOffScreen(navController = navController)
        return
    }

    SearchingForNetworksScreen(
        modifier = Modifier,
        navController = navController,
        navigateToWifiAlreadyConnected = vm::navigateToWifiAlreadyConnected,
        setWifiFlow = vm::setWifiFlow,
        onCancelClicked = vm::onCancelClicked,
        navigateToWifiNetworksScreen = vm::navigateToWifiNetworksScreen,
        navigateToNoWifiScreen = vm::navigateToNoNetworksScreen,
        uiState = uiState,
        loadingText = stringResource(id = com.esightcorp.mobile.app.ui.R.string.kWifiSearchSpinnerTitle)
    )
}

//region Private implementation
private const val TAG = "SearchingForNetworksScreen"

@Composable
internal fun SearchingForNetworksScreen(
    modifier: Modifier = Modifier,
    loadingText: String,
    navigateToWifiAlreadyConnected: OnNavigationCallback,
    navigateToWifiNetworksScreen: OnNavigationCallback,
    navigateToNoWifiScreen: OnNavigationCallback,
    onCancelClicked: OnNavigationCallback,
    setWifiFlow: (String) -> Unit,
    navController: NavController,
    uiState: WifiSearchingUiState
) {
    BackHandler { onCancelClicked(navController) }

    when (uiState.scanningStatus) {
        ScanningStatus.Failed -> {
            Log.e(TAG, "SearchingForNetworksScreen: SCAN STATUS FAILED")
            LaunchedEffect(Unit) {
                navigateToNoWifiScreen(navController)
            }
        }

        ScanningStatus.Success -> {
            Log.d(TAG, "SearchingForNetworksScreen: SUCCESS")
            LaunchedEffect(Unit) {
                navigateToWifiNetworksScreen(navController)
            }
        }

        else -> {
            Log.i(TAG, "Searching for networks...")
            Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
                LoadingScreenWithSpinner(
                    loadingText = loadingText,
                    modifier = modifier,
                    cancelButtonNeeded = true,
                    onCancelButtonClicked = { onCancelClicked(navController) })
            }
            when (uiState.wifiConnectionStatus) {
                CONNECTED -> {
                    Log.d(TAG, "go to already connected")
                    LaunchedEffect(Unit) {
                        delay(2000L)
                        navigateToWifiAlreadyConnected(navController)
                    }
                }

                DISCONNECTED -> {
                    Log.i(TAG, "SearchingForNetworksScreen: Disconnected flow")
                    LaunchedEffect(Unit) {
                        setWifiFlow(WifiNavigation.ScanningRoute.PARAM_BLUETOOTH)
                    }
                }

                UNKNOWN -> {
                    Log.i(TAG, " Not sure if we are already connected or not")
                }
            }
        }
    }
}
//endregion
