/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectingUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectingViewModel
import kotlinx.coroutines.delay

@Composable
fun WifiConnectingRoute(
    navController: NavController,
    vm: WifiConnectingViewModel = hiltViewModel(),
) {
    val isConnected by vm.connectionStateFlow().collectAsState()
    if (isConnected == false) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    val uiState by vm.uiState.collectAsState()

    WifiConnectingScreen(
        navController = navController,
        uiState = uiState,
        onWifiConfigureSuccess = vm::navigateToWifiConnected,
        onWifiConfigureFail = vm::navigateToWifiConnectError,
    )
}

//region Internal implementation
private const val TAG = "WifiConnectingScreen"

@Composable
private fun WifiConnectingScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    uiState: WifiConnectingUiState,
    onWifiConfigureSuccess: OnNavigationCallback? = null,
    onWifiConfigureFail: OnNavigationCallback? = null,
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        LoadingScreenWithSpinner(
            modifier = modifier,
            loadingText = stringResource(
                R.string.kWifiViewControllerWifiConnectingSpinnerTitle
            ),
            cancelButtonNeeded = false
        )
    }

    if (uiState.connectionWasSuccess) {
        LaunchedEffect(Unit) {
            delay(SCREEN_DELAY)

            onWifiConfigureSuccess?.invoke(navController)
        }
    }

    if (uiState.connectionError) {
        LaunchedEffect(Unit) {
            delay(SCREEN_DELAY)

            Log.e(TAG, "WifiConnectingScreen: ERROR connecting to wifi network provided ")
            onWifiConfigureFail?.invoke(navController)
        }
    }
}

private const val SCREEN_DELAY = 2000L

@Preview
@Composable
private fun WifiConnectingScreenPreview() = MaterialTheme {
    WifiConnectingScreen(
        navController = rememberNavController(),
        uiState = WifiConnectingUiState(ssid = "My Network")
    )
}
//endregion
