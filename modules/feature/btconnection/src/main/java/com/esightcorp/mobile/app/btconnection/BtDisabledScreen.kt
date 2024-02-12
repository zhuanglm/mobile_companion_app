/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.viewmodels.BtDisabledViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.SubHeader
import com.esightcorp.mobile.app.ui.extensions.navigate
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun BtDisabledRoute(
    navController: NavController,
    vm: BtDisabledViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()

    when (uiState.isBtEnabled) {
        false -> {
            BtDisabledScreenImpl(
                navController = navController,
                onBtStateChanged = vm::updateBtEnabledState
            )
        }

        true -> {
            Log.d(TAG, "BtDisabledScreen - BT is now enabled!")
            LaunchedEffect(Unit) { vm.onBtEnabled(navController) }
        }
    }
}

//region Internal implementation

private const val TAG = "BtDisabledScreen"

@Composable
private fun BtDisabledScreenImpl(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBtStateChanged: (Boolean) -> Unit,
) {
    BtDisabledBody(modifier = modifier,
        navController = navController,
        onReconnectCallback = { nav ->
            nav.navigate(target = BtConnectionNavigation.BtSearchingRoute, popCurrent = true)
        },
        onCancelCallback = { nav ->
            nav.navigate(target = BtConnectionNavigation.NoDeviceConnectedRoute, popCurrent = true)
        })

    // If Bluetooth is not enabled, launch system dialog to enable Bluetooth
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            Log.d(TAG, "isBluetoothEnabled: $it")
            onBtStateChanged.invoke(it.resultCode == Activity.RESULT_OK)
        }
    )

    DisposableEffect(Unit) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        launcher.launch(intent)
        onDispose {
            // clean up any resources if needed
        }
    }
}

@Composable
private fun BtDisabledBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    onReconnectCallback: OnNavigationCallback? = null,
    onCancelCallback: OnNavigationCallback? = null,
) = BaseScreen(
    modifier = modifier,
    showBackButton = true,
    onBackButtonInvoked = { onCancelCallback?.invoke(navController) },
    showSettingsButton = false,
    bottomButton = { },
) {
    ItemSpacer(106.dp)
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(
            drawableId = R.drawable.baseline_bluetooth_24,
            contentDescription = stringResource(id = R.string.kAccessibilityIconBluetooth)
        )
        ItemSpacer(25.dp)

        // Set up the header text
        Header1Text(
            text = stringResource(R.string.kBTErrorBluetoothOffTitle),
            modifier = modifier,
        )
        ItemSpacer(15.dp)

        // Set up the body text
        SubHeader(
            text = stringResource(R.string.kBTErrorBluetoothOffDescription),
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        ItemSpacer(50.dp)
        TextRectangularButton(
            onClick = { onReconnectCallback?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kRetryButtonTitle),
            textAlign = TextAlign.Center,
        )

        ItemSpacer(20.dp)
        OutlinedTextRectangularButton(
            onClick = { onCancelCallback?.invoke(navController) },
            modifier = modifier,
            text = stringResource(R.string.kCancel),
            textAlign = TextAlign.Center,
            textColor = MaterialTheme.colors.onSurface,
        )
    }
}

@Preview
@Composable
private fun BtDisabledBodyPreview() = MaterialTheme {
    BtDisabledBody(navController = rememberNavController())
}

//endregion
