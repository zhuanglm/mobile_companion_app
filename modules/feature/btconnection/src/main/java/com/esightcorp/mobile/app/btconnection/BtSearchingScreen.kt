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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.state.BtSearchingUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtSearchingViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.utils.ScanningStatus

@Composable
fun BtSearchingRoute(
    navController: NavController,
    vm: BtSearchingViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()

    BackStackLogger(navController, TAG)

    if (uiState.isBtEnabled == false) {
        ExecuteOnce { vm.navigateToBtDisabled(navController) }
        return
    }

    BtSearchingScreen(
        modifier = Modifier,
        navController = navController,
        uiState = uiState,
        onCancelButtonClicked = vm::onCancelButtonClicked,
        onStartScanning = vm::triggerScan,
        onScanSuccess = vm::onScanSuccess,
    )
}

@Preview
@Composable
fun BtSearchingScreenPreview() = MaterialTheme {
    BtSearchingScreen(
        modifier = Modifier,
        navController = rememberNavController(),
        uiState = BtSearchingUiState(isBtEnabled = true),
        onCancelButtonClicked = {},
        onStartScanning = {},
        onScanSuccess = {},
    )
}

//region Internal implementation

private const val TAG = "BtSearchingScreen"

@Composable
internal fun BtSearchingScreen(
    modifier: Modifier,
    navController: NavController,
    uiState: BtSearchingUiState,
    onCancelButtonClicked: OnNavigationCallback,
    onStartScanning: OnActionCallback,
    onScanSuccess: OnNavigationCallback,
) {
    when (uiState.isScanning) {
        ScanningStatus.Failed -> {
            Log.e(TAG, "Bluetooth Scanning has failed. Show the error screen")
            //TODO: implement this!!!
        }

        ScanningStatus.Success -> {
            Log.i(TAG, "BtSearchingScreen: Navigating to devices route")

            ExecuteOnce { onScanSuccess.invoke(navController) }
        }

        else -> {
            LoadingScreenWithSpinner(
                loadingText = stringResource(id = R.string.kBTSearchSpinnerTitle),
                modifier = modifier,
                onCancelButtonClicked = { onCancelButtonClicked(navController) },
            )
            if (uiState.isScanning == ScanningStatus.Unknown) {
                ExecuteOnce { onStartScanning.invoke() }
            }
        }
    }
}
//endregion
