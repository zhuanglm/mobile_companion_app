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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.home.viewmodels.LocationServiceOffViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.PermissionScreen
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback


@Composable
fun LocationServiceOffRoute(
    navController: NavController,
    vm: LocationServiceOffViewModel = hiltViewModel(),
) {
    val locationSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { vm.verifyLocationServiceState() },
    )

    LocationServiceOffScreen(
        navController = navController,
        onOkPressed = { vm.gotoSystemLocationSettings(locationSettingLauncher) },
        onCancelPressed = vm::onDismiss,
    )

    val isLocationEnabled by vm.isLocationServiceEnabled.collectAsState()
    Log.d(TAG, "->> isLocationEnabled: $isLocationEnabled")
    when (isLocationEnabled) {
        true -> ExecuteOnce { vm.onDismiss(navController) }
        else -> Unit
    }
}

//region Private implementation
private const val TAG = "LocationServiceOffScreen"

@Composable
private fun LocationServiceOffScreen(
    navController: NavController,
    onCancelPressed: OnNavigationCallback? = null,
    onOkPressed: OnNavigationCallback? = null,
) = PermissionScreen(
    navController = navController,
    titleId = R.string.kPermissionLocationServicesTitle,
    descriptionId = R.string.kPermissionLocationServicesBody,
    okLabelId = R.string.kSettingsViewTitleText,
    onCancelPressed = onCancelPressed,
    onOkPressed = onOkPressed,
)

@Preview
@Composable
private fun LocationServiceOffScreenPreview() = MaterialTheme {
    LocationServiceOffScreen(navController = rememberNavController())
}
//endregion
