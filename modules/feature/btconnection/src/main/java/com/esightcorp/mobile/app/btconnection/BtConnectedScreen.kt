/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

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
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import kotlinx.coroutines.delay

@Composable
fun BtConnectedRoute(
    navController: NavController,
    vm: BtConnectedViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    if (!uiState.isBtEnabled) {
        ExecuteOnce{vm.gotoBtDisabledScreen(navController)}
        return
    } else {
        BtConnectedScreen(
            navController = navController,
            deviceAddress = uiState.deviceAddress,
            deviceName = uiState.deviceName,
            goToUnableToConnectScreen = vm::goToUnableToConnectScreen,
            gotoHomeScreen = { nav ->
                vm.gotoMainScreen(nav = nav, popUntil = BtConnectionNavigation.IncomingRoute)
            },
        )
    }
}

//region Internal implementation

@Composable
private fun BtConnectedScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    deviceAddress: String? = null,
    deviceName: String? = null,
    gotoHomeScreen: OnNavigationCallback? = null,
    goToUnableToConnectScreen: OnNavigationCallback? = null,
) {
    if (deviceName != null && deviceAddress != null) {
        val loadingText = stringResource(id = R.string.kBTPairingConnectedTitle)
        LoadingScreenWithIcon(modifier = modifier, loadingText = loadingText)

        LaunchedEffect(Unit) {
            delay(SCREEN_TIMEOUT)

            gotoHomeScreen?.invoke(navController)
        }
    }
    else {
        LaunchedEffect(Unit) {
            goToUnableToConnectScreen?.invoke(navController)
        }
    }
}

@Preview
@Composable
private fun BtConnectedScreenPreview() = MaterialTheme {
    BtConnectedScreen(navController = rememberNavController())
}

private const val SCREEN_TIMEOUT = 5000L //5s in milliseconds
// endregion
