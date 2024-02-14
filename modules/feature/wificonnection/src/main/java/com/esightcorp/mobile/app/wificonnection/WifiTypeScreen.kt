/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.networking.WifiType
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.TrailingIconButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.wificonnection.state.WifiTypeUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiTypeViewModel

@Composable
fun WifiTypeRoute(
    navController: NavController, vm: WifiTypeViewModel = hiltViewModel()
) {
    val wifiUiState by vm.uiState.collectAsState()
    BackStackLogger(navController = navController)

    WifiTypeScreen(
        navController = navController,
        modifier = Modifier,
        wifiUiState = wifiUiState,
        onTypePressed = vm::onTypePressed,
    )
}


@Composable
internal fun WifiTypeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    wifiUiState: WifiTypeUiState,
    onTypePressed: (NavController, WifiType) -> Unit,
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = false ,
        showSettingsButton = false ,
        onBackButtonInvoked = { /*unused*/ },
        onSettingsButtonInvoked = { /*unused*/ },
        isBottomButtonNeeded = false,
        bottomButton = { }) {
        WifiTypeBody(
            modifier = modifier,
            navController = navController,
            wifiUiState = wifiUiState,
            onTypePressed = onTypePressed,
        )

    }
}


@Preview
@Composable
private fun WifiTypePreview() {
    WifiTypeBody(modifier = Modifier,
        navController = rememberNavController(),
        wifiUiState = WifiTypeUiState(),
        onTypePressed = {_, _ ->  }
    )
}

@Composable
private fun WifiTypeBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    wifiUiState: WifiTypeUiState,
    onTypePressed: (NavController, WifiType) -> Unit,
){
    Column {
        Header1Text(
            stringResource(R.string.kWifiViewControllerSelectSecurityLabelText),
            modifier,
        )

        ItemSpacer(25.dp)
        TrailingIconButton(
            onClick = { onTypePressed(navController, WifiType.WPA) },
            modifier = modifier,
            icon = if(wifiUiState.wifiType == WifiType.WPA) Icons.Rounded.Check else null,
            text = stringResource(id = R.string.kWifiSecurityTypeWPA)
        )

        ItemSpacer(20.dp)
        TrailingIconButton(
            onClick = { onTypePressed(navController, WifiType.WEP) },
            modifier = modifier,
            icon = if(wifiUiState.wifiType == WifiType.WEP) Icons.Rounded.Check else null,
            text = stringResource(id = R.string.kWifiSecurityTypeWEP)
        )

        ItemSpacer(20.dp)
        TrailingIconButton(
            onClick = { onTypePressed(navController, WifiType.NONE) },
            modifier = modifier,
            icon = if(wifiUiState.wifiType == WifiType.NONE) Icons.Rounded.Check else null,
            text = stringResource(id = R.string.kWifiSecurityTypeNone)
        )
    }
}
