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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.home.viewmodels.LocationPermissionViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.PermissionScreen
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.utils.findActivity
import com.esightcorp.mobile.app.utils.permission.PermissionUiState


@Composable
fun LocationPermissionRoute(
    navController: NavController,
    vm: LocationPermissionViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = vm::onPermissionsUpdated,
    )
    val context = LocalContext.current

    val appSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = vm::onAppSettingResult,
    )

    ExecuteOnce { vm.registerPermissionLauncher(permissionLauncher, context.findActivity()) }

    LocationPermissionScreen(
        navController = navController,
        onCancelPressed = vm::onDismiss,
        onOpenAppSettingPressed = { vm.gotoAppSetting(appSettingLauncher) }
    )

    DisposableEffect(Unit) { onDispose { vm.cleanUp() } }

    val permissionUiState by vm.permissionUiState.collectAsState()
    Log.w(TAG, "permissionUiState: ${permissionUiState.state}")
    if (permissionUiState.state == PermissionUiState.PermissionState.GRANTED) {
        // Permission granted, just stop this flow
        ExecuteOnce { vm.onDismiss(navController) }
        return
    }
}

//region Private Implementation
private const val TAG = "LocationPermissionScreen"

@Composable
private fun LocationPermissionScreen(
    navController: NavController,
    onCancelPressed: OnNavigationCallback? = null,
    onOpenAppSettingPressed: OnNavigationCallback? = null,
) = PermissionScreen(
    navController = navController,
    descriptionId = R.string.kPermissionLocation,
    onCancelPressed = onCancelPressed,
    onOpenAppSettingPressed = onOpenAppSettingPressed
)

@Preview
@Composable
private fun LocationPermissionScreenPreview() = MaterialTheme {
    LocationPermissionScreen(
        navController = rememberNavController()
    )
}

//endregion
