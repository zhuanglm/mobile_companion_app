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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.home.state.HomePermissionUiState.RationaleReason
import com.esightcorp.mobile.app.home.viewmodels.PermissionViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.containers.Centered
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.EnumSet

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePermissionScreen(
    navController: NavController,
    vm: PermissionViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)
    val uiState by vm.uiState.collectAsState()

    val permissionsState = rememberMultiplePermissionsState(permissions = vm.requiredPermissions) {
        // Permission request completed, update status
        vm.onPermissionsUpdated(it)
    }

    vm.debugPermissionsState(permissionsState)

    if (permissionsState.allPermissionsGranted) {
        LaunchedEffect(Unit) { vm.navigateToBluetoothConnection(navController) }
        return
    }

    // Initialize the current permissions status once
    LaunchedEffect(Unit) { vm.verifyPermissionStates(permissionsState) }

    val requestPermissionsCallback =
        remember { mutableStateOf({ permissionsState.launchMultiplePermissionRequest() }) }

    val appSettingLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i(TAG, "->> Back from AppSettings, refresh permissions status again ...")
            requestPermissionsCallback.value.invoke()
        }

    val gotoAppSettingCallback =
        remember { mutableStateOf({ vm.navigateToAppSettings(appSettingLauncher) }) }

    PermissionScreen(
        onRequestPermissions = requestPermissionsCallback.value,
        onOpenAppSettings = gotoAppSettingCallback.value,
        rationaleReasons = uiState.rationaleReasons,
        useSystemPopup = permissionsState.shouldShowRationale,
    )
}

//region Internal implementation

private const val TAG = "HomePermissionScreen"

@Composable
private fun PermissionScreen(
    modifier: Modifier = Modifier,
    onRequestPermissions: OnActionCallback? = null,
    onOpenAppSettings: OnActionCallback? = null,
    rationaleReasons: EnumSet<RationaleReason>? = null,
    useSystemPopup: Boolean = false,
) = BaseScreen(
    modifier = modifier,
    showBackButton = false,
    showSettingsButton = false,
    isContentCentered = true,
    bottomButton = { },
) {
    Centered {
        BigIcon(drawableId = R.drawable.warning,
            contentDescription = stringResource(id = R.string.kAccessibilityIconExclamation))

        ItemSpacer(30.dp)
        Header1Text(
            text = stringResource(R.string.kPermissionRequiredTitle),
            modifier = modifier
        )
        ItemSpacer(30.dp)

        when (rationaleReasons) {
            null -> {
                BoldSubheader(
                    text = stringResource(R.string.label_home_screen_request_permission_description),
                    modifier = modifier,
                    textAlign = TextAlign.Center
                )
            }

            else -> rationaleReasons.forEach { reason: RationaleReason ->
                when (reason) {
                    RationaleReason.FOR_BLUETOOTH -> {
                        BoldSubheader(
                            text = stringResource(R.string.kPermissionBluetooth),
                            modifier = modifier.padding(vertical = 10.dp),
                            color = MaterialTheme.colors.onSurface,
                        )
                    }

                    RationaleReason.FOR_LOCATION -> {
                        BoldSubheader(
                            text = stringResource(R.string.kPermissionLocation),
                            modifier = modifier.padding(vertical = 10.dp),
                            color = MaterialTheme.colors.onSurface,
                        )
                    }
                }
            }
        }
        ItemSpacer(60.dp)

        TextRectangularButton(
            modifier = modifier,
            enabled = (rationaleReasons == null || useSystemPopup),
            text = stringResource(R.string.kPermissionRequestButton),
            onClick = { onRequestPermissions?.invoke() },
            textAlign = TextAlign.Center,
        )
        ItemSpacer(30.dp)

        OutlinedTextRectangularButton(
            onClick = { onOpenAppSettings?.invoke() },
            modifier = modifier,
            text = stringResource(R.string.kPermissionOpenSettingsButton),
            textAlign = TextAlign.Center,
            textColor = MaterialTheme.colors.onSurface,
        )
    }
}

@Preview
@Composable
private fun PermissionScreenPreview() = MaterialTheme {
    PermissionScreen()
}

//endregion
