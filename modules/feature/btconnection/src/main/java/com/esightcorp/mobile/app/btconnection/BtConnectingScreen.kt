/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.material.MaterialTheme
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
import com.esightcorp.mobile.app.btconnection.state.BtConnectingUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectingViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun BtConnectingRoute(
    navController: NavController,
    vm: BtConnectingViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    when (uiState.isBtEnabled) {
        true -> BtConnectingScreen(
            navController = navController,
            uiState = uiState,
            onDeviceConnected = vm::navigateToConnectedScreen,
            onDeviceConnectFailed = vm::navigateToUnableToConnectScreen,
        )

        false -> ExecuteOnce { vm.navigateToBtDisabledScreen(navController) }
    }
}

//region Internal implementation
private const val TAG = "BtConnectingScreen"

@Composable
private fun BtConnectingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: BtConnectingUiState,
    onDeviceConnected: OnNavigationCallback? = null,
    onDeviceConnectFailed: OnNavigationCallback? = null,
) {
    when (uiState.didDeviceConnect) {
        null -> {
            LoadingScreenWithSpinner(
                modifier = modifier,
                loadingText = stringResource(R.string.kBTConnectSpinnerTitle),
                cancelButtonNeeded = false,
            )
        }

        true -> LaunchedEffect(Unit) { onDeviceConnected?.invoke(navController) }

        false -> LaunchedEffect(Unit) {
            Log.e(TAG, "BtConnectingScreen: We did not connect, go to error screen")
            onDeviceConnectFailed?.invoke(navController)
        }
    }
}

@Preview
@Composable
private fun BtConnectingScreenConnectingPreview() = MaterialTheme {
    BtConnectingScreen(
        navController = rememberNavController(),
        uiState = BtConnectingUiState(),
    )
}
//endregion
