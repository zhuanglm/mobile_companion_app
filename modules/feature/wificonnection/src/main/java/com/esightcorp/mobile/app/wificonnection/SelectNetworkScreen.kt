/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import android.net.wifi.ScanResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.networking.ssidName
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.LeadingIconTextButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.AdvancedSettingsButton
import com.esightcorp.mobile.app.ui.components.containers.HomeBaseScreen
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.wificonnection.state.SelectNetworkUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.SelectNetworkViewModel


@Composable
fun SelectNetworkRoute(
    navController: NavController,
    vm: SelectNetworkViewModel = hiltViewModel()
) {
    BackStackLogger(navController, TAG)

    val isConnected by vm.connectionStateFlow().collectAsState()
    if (isConnected == false) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    val uiState by vm.uiState.collectAsState()
    if (!uiState.isWifiEnabled) {
        NavigateToWifiOffScreen(navController = navController)
        return
    }

    if (uiState.networkList.isEmpty()) {
        LaunchedEffect(Unit) { vm.navigateToNoNetworksFoundScreen(navController) }
        return
    }

    SelectNetworkScreen(
        modifier = Modifier,
        navController = navController,
        uiState = uiState,
        onBackButtonClicked = vm::onBackButtonClicked,
        onAdvancedButtonClicked = vm::onAdvancedButtonClicked,
        onNetworkSelected = vm::onNetworkSelected,
    )
}

//region Private Implementation
private const val TAG = "SelectNetworkScreen"

@Composable
internal fun SelectNetworkScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: SelectNetworkUiState,
    onBackButtonClicked: (NavController) -> Unit,
    onAdvancedButtonClicked: (NavController) -> Unit,
    onNetworkSelected: (NavController, ScanResult) -> Unit,
) {
    HomeBaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackButtonClicked(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = {
            AdvancedSettingsButton(
                modifier = modifier,
                onAdvancedSettingsClick = { onAdvancedButtonClicked(navController) },
            )
        },
    ) {
        SelectNetworkBody(
            modifier = modifier,
            navController = navController,
            uiState = uiState,
            onNetworkSelected = onNetworkSelected,
        )
    }
}

@Composable
private fun SelectNetworkBody(
    modifier: Modifier,
    navController: NavController,
    uiState: SelectNetworkUiState,
    onNetworkSelected: (NavController, ScanResult) -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, networkContainer) = createRefs()
        Header1Text(
            text = stringResource(R.string.kWifiViewSelectWifiNameLabelText),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 35.dp)
                start.linkTo(parent.start)
            },
        )

        LazyColumn(
            modifier = modifier.constrainAs(networkContainer) {
                top.linkTo(header.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            },
        ) {
            items(uiState.networkList) { network ->
                LeadingIconTextButton(
                    onClick = { onNetworkSelected(navController, network) },
                    modifier = Modifier,
                    icon = ImageVector.vectorResource(R.drawable.round_wifi_24),
                    text = network.ssidName() ?: ""
                )
                ItemSpacer(space = 10.dp)
            }
        }
    }
}

@Preview
@Composable
private fun SelectNetworkScreenPreview() = MaterialTheme {
    SelectNetworkScreen(
        navController = rememberNavController(),
        uiState = SelectNetworkUiState(),
        onBackButtonClicked = { },
        onAdvancedButtonClicked = { },
        onNetworkSelected = { _, _ -> },
    )
}
//endregion
