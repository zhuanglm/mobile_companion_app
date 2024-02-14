/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import com.esightcorp.mobile.app.btconnection.repositories.BtUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtPermissionViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.PermissionScreen
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.utils.findActivity
import com.esightcorp.mobile.app.utils.permission.PermissionUiState

@Composable
fun BtPermissionRoute(
    navController: NavController,
    vm: BtPermissionViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = vm::onPermissionsUpdated,
    )
    val context = LocalContext.current

    val appSettingLauncher = rememberLauncherForActivityResult(
        contract = StartActivityForResult(),
        onResult = vm::onAppSettingResult,
    )

    BtPermissionScreen(
        navController = navController,
        onCancelPressed = { it.popBackStack() },
        onOpenAppSettingPressed = {
            vm.registerPermissionLauncher(permissionLauncher, context.findActivity())
            vm.gotoAppSetting(appSettingLauncher)
        },
    )

    DisposableEffect(Unit) { onDispose { vm.cleanUp() } }

    val permissionUiState by vm.permissionUiState.collectAsState()
    Log.w(TAG, "permissionUiState: ${permissionUiState.state}")
    if (permissionUiState.state == PermissionUiState.PermissionState.GRANTED) {
        val btHwState by vm.btUiState.collectAsState()
        Log.w(TAG, "btHwState: ${btHwState.state}")
        ExecuteOnce(key = btHwState.state) {
            when (btHwState.state) {
                BtUiState.BtHwState.ENABLED -> vm.navigateToBtSearching(navController)
                BtUiState.BtHwState.DISABLED -> vm.navigateToBtDisabled(navController)
                else -> vm.checkBtHwState()
            }
        }
        return
    }
}

//region Private implementation
private const val TAG = "BtPermissionScreen"

@Composable
private fun BtPermissionScreen(
    navController: NavController,
    onCancelPressed: OnNavigationCallback? = null,
    onOpenAppSettingPressed: OnNavigationCallback? = null,
) = PermissionScreen(
    navController = navController,
    descriptionId = R.string.kPermissionBluetooth,
    onCancelPressed = onCancelPressed,
    onOpenAppSettingPressed = onOpenAppSettingPressed
)

@Preview
@Composable
private fun BtPermissionScreenPreview() = MaterialTheme {
    BtPermissionScreen(
        navController = rememberNavController(),
    )
}
//endregion
