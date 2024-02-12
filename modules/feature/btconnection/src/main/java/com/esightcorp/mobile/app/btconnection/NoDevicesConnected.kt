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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesConnectedViewModel
import com.esightcorp.mobile.app.ui.components.ExecuteOnce
import com.esightcorp.mobile.app.ui.components.buttons.AddDeviceButton
import com.esightcorp.mobile.app.ui.components.buttons.TermsAndPolicy
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.FeedbackButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.PersonalGreeting
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.utils.findActivity
import com.esightcorp.mobile.app.utils.permission.PermissionUiState.PermissionState

@Composable
fun NoDeviceConnectedRoute(
    navController: NavController,
    vm: NoDevicesConnectedViewModel = hiltViewModel(),
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = vm::onPermissionsUpdated
    )
    val context = LocalContext.current

    NoDeviceConnectedScreen(
        onScanESightPressed = {
            vm.registerPermissionLauncher(permissionLauncher, context.findActivity())
            vm.initPermissionCheck()
        },
        onSettingsButtonPressed = vm::navigateToSettings,
        onFeedbackButtonPressed = vm::gotoEsightFeedbackSite,
        onTermsAndConditionsPressed = vm::gotoEsightPrivacyPolicySite,
        onPrivacyPolicyPressed = vm::gotoEsightPrivacyPolicySite,
        navController = navController
    )

    DisposableEffect(Unit) { onDispose { vm.cleanUp() } }

    // Verify Bt state
    val btPermissionState by vm.permissionUiState.collectAsState()
    Log.d(TAG, "btPermissionState: ${btPermissionState.state}")
    when (btPermissionState.state) {
        PermissionState.GRANTED -> {
            ExecuteOnce { vm.navigateToBtScanning(navController) }
            return
        }

        PermissionState.SHOW_RATIONALE -> {
            ExecuteOnce { vm.navigateToBtPermission(navController) }
            return
        }

        else -> Unit
    }

//    when (uiState.bluetoothState) {
//        BluetoothState.READY -> ExecuteOnce { vm.navigateToBtScanning(navController) }
//
//        BluetoothState.DISABLED -> ExecuteOnce { vm.navigateToBtDisabled(navController) }
//
//        else -> Unit
//    }
}

//region Private implementation
private const val TAG = "NoDevicesConnected"

@Composable
private fun NoDeviceConnectedScreen(
    modifier: Modifier = Modifier,
    onScanESightPressed: OnActionCallback? = null,
    onSettingsButtonPressed: OnNavigationCallback? = null,
    onFeedbackButtonPressed: OnActionCallback? = null,
    onTermsAndConditionsPressed: () -> Unit,
    onPrivacyPolicyPressed: () -> Unit,
    navController: NavController,
) {
    BaseScreen(modifier = modifier,
        showBackButton = false,
        showSettingsButton = true,
        onSettingsButtonInvoked = { onSettingsButtonPressed?.invoke(navController) },
        bottomButton = {
            FeedbackButton(
                modifier = modifier,
                onFeedbackClick = { onFeedbackButtonPressed?.invoke() },
            )
        },
        bottomAlignedContent = {
            TermsAndPolicy(
                onTermsInvoked = onTermsAndConditionsPressed,
                onPrivacyPolicyInvoked = onPrivacyPolicyPressed,
                modifier = modifier,
                textColor = MaterialTheme.colors.onSurface
            )
        }) {
        NoDevicesBody(
            modifier = modifier,
            onScanESightPressed = onScanESightPressed,
        )
    }
}

@Composable
private fun NoDevicesBody(
    modifier: Modifier,
    onScanESightPressed: OnActionCallback? = null,
) {
    ConstraintLayout(modifier = modifier) {
        val (greeting, deviceButton) = createRefs()

        // Set up greeting message
        PersonalGreeting(
            modifier = modifier.constrainAs(greeting) {
                top.linkTo(parent.top)
            },
            connected = false,
        )

        // Set up device button to navigate to Bluetooth searching screen
        val btnClicked = remember { mutableStateOf(false) }

        AddDeviceButton(
            onClick = {
                if (btnClicked.value) return@AddDeviceButton

                onScanESightPressed?.invoke()
                btnClicked.value = true
            },
            modifier = modifier.constrainAs(deviceButton) {
                top.linkTo(greeting.bottom, margin = 25.dp)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoDeviceConnectedScreenPreview() = MaterialTheme {
    NoDeviceConnectedScreen(
        onTermsAndConditionsPressed = { },
        onPrivacyPolicyPressed = { },
        navController = rememberNavController(), // Note: this won't actually navigate in preview
        modifier = Modifier
    )
}

//endregion
