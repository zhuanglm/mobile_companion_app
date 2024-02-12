/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.state.BtDevicesUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtDevicesViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.YellowDeviceCard
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton

@Composable
fun BtDevicesRoute(
    navController: NavController,
    vm: BtDevicesViewModel = hiltViewModel(),
) {
    vm.getDeviceList()
    val uiState by vm.uiState.collectAsState()
    if (!uiState.isBtEnabled) {
        NavigateBluetoothDisabled(navController = navController)
    } else if (uiState.listOfAvailableDevices.isEmpty()) {
        LaunchedEffect(Unit) { vm.navigateToUnableToFindESight(navController) }
    } else {
        BtDevicesScreen(
            navController = navController,
            uiState = uiState,
            onBackClicked = vm::navigateToNoDeviceConnectedScreen,
            onDeviceSelected = vm::navigateToBtConnectingScreen,
            onHelpClicked = vm::navigateToUnableToFindESight,
        )
    }
}

@Preview
@Composable
fun BtDevicesScreenPreview() = MaterialTheme {
    BtDevicesScreen(
        navController = rememberNavController(),
        uiState = BtDevicesUiState(listOfAvailableDevices = listOf("ABC")),
        onBackClicked = {},
        onDeviceSelected = { _, _ -> },
        onHelpClicked = {}
    )
}

//region Internal implementation
private const val TAG = "BtDevicesScreen"

@Composable
internal fun BtDevicesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: BtDevicesUiState,
    onBackClicked: (NavController) -> Unit,
    onDeviceSelected: (NavController, String) -> Unit,
    onHelpClicked: (NavController) -> Unit,
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout {
            val (topBar, header, deviceContainer, help) = createRefs()

//            Log.w(
//                TAG,
//                "Back-stack:\n${navController.currentBackStack.collectAsState().value.toStringList()}"
//            )

            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { onBackClicked.invoke(navController) },
                onSettingsButtonInvoked = { },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )

            /*
            Have to bring the margins in as vals since the margin function in .constrainAs
            does not accept a @Composable function, but does accept a value
             */
            val headerMargin = dimensionResource(id = R.dimen.bt_devices_header_margin)
            val lazyColTopMargin = dimensionResource(id = R.dimen.lazy_col_top_margin)

            Header1Text(
                text = stringResource(id = R.string.kBTPairingHeader),
                modifier = modifier
                    .padding(horizontal = dimensionResource(R.dimen.header_horizontal_padding))
                    .constrainAs(header) {
                        top.linkTo(topBar.bottom, margin = headerMargin)
                        start.linkTo(parent.start)
                    },
            )

            LazyColumn(
                modifier = modifier.constrainAs(deviceContainer) {
                    top.linkTo(header.bottom, margin = lazyColTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(help.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            ) {
                items(uiState.listOfAvailableDevices) { device ->

                    if (device.contains(stringResource(id = R.string.hyphen))) {
                        val serialNumber =
                            device.substringAfterLast(stringResource(id = R.string.hyphen))
                        YellowDeviceCard(
                            serialNumber = serialNumber,
                            modifier = modifier.padding(dimensionResource(id = R.dimen.yellow_device_card_padding)),
                            onClick = {
                                Log.i(TAG, "$device was selected. Trying to connect...")
                                onDeviceSelected.invoke(navController, device)
                            },
                        )
                    } else {
                        val serialNumber = stringResource(id = R.string.default_serial_number)
                        YellowDeviceCard(
                            serialNumber = serialNumber,
                            modifier = modifier.padding(dimensionResource(id = R.dimen.yellow_device_card_padding)),
                            onClick = { Log.e(TAG, "This device is not an eSight device") },
                        )
                    }
                }
            }

            CantFindDeviceButton(
                modifier = modifier
                    .padding(top = dimensionResource(R.dimen.bottom_button_top_padding))
                    .constrainAs(help) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onHelpClick = { onHelpClicked.invoke(navController) }
            )
        }
    }
}

//endregion
