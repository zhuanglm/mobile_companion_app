/*
 * LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.CustomEditText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.PasswordEditText
import com.esightcorp.mobile.app.wificonnection.state.WifiAdvancedSettingsUiState
import com.esightcorp.mobile.app.wificonnection.state.WifiType
import com.esightcorp.mobile.app.wificonnection.viewmodels.AdvancedWifiViewModel

@Composable
fun AdvancedWifiRoute(
    navController: NavController,
    vm: AdvancedWifiViewModel = hiltViewModel()
) {
    vm.refreshUiState()

    val isConnected by vm.connectionStateFlow().collectAsState()
    if (isConnected == false) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    AdvancedWifiScreen(
        navController = navController,
        modifier = Modifier,
        vm = vm,
        onSsidUpdated = vm::onSsidUpdated,
        onPasswordUpdated = vm::onPasswordUpdated,
        onBackButtonPressed = vm::onBackButtonPressed,
        onSecurityButtonPressed = vm::onSecurityButtonPressed,
        onFinishButtonPressed = vm::onFinishButtonPressed
    )
}

@Composable
internal fun AdvancedWifiScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: AdvancedWifiViewModel,
    onSsidUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onBackButtonPressed: (NavController) -> Unit,
    onSecurityButtonPressed: (NavController) -> Unit,
    onFinishButtonPressed: (NavController) -> Unit,
) {
    val wifiUiState by vm.uiState.collectAsState()

    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackButtonPressed(navController) },
        onSettingsButtonInvoked = { /*Unused*/ },
        isBottomButtonNeeded = false,
        bottomButton = { }) {

        ItemSpacer(35.dp)
        AdvancedWifiBody(
            modifier = modifier,
            navController = navController,
            wifiUiState = wifiUiState,
            onSsidUpdated = onSsidUpdated,
            onPasswordUpdated = onPasswordUpdated,
            onSecurityButtonPressed = onSecurityButtonPressed,
            onFinishButtonPressed = onFinishButtonPressed,

        )
    }
}

@Preview
@Composable
private fun WifiTypePreview() {
    AdvancedWifiBody(modifier = Modifier,
        navController = rememberNavController(),
        wifiUiState = WifiAdvancedSettingsUiState(),
        onSsidUpdated = {_ ->  },
        onPasswordUpdated = {_ ->},
        onSecurityButtonPressed = {_ ->},
        onFinishButtonPressed = { _ ->  }
    )
}
@Composable
private fun AdvancedWifiBody(
    modifier: Modifier,
    navController: NavController,
    wifiUiState: WifiAdvancedSettingsUiState,
    onSsidUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onSecurityButtonPressed: (NavController) -> Unit,
    onFinishButtonPressed: (NavController) -> Unit,
){
    Column {
        Header1Text(
            stringResource(R.string.kWifiViewWifiNameLabelText),
            modifier,
        )

        ItemSpacer(25.dp)
        CustomEditText(value = wifiUiState.ssid,
            onValueChange = onSsidUpdated,
            modifier = modifier.fillMaxWidth())

        if(wifiUiState.wifiType != WifiType.NONE) {
            ItemSpacer(35.dp)
            Header1Text(
                text = stringResource(id = R.string.kWifiViewWifiPasswordLabelText),
                modifier = modifier
            )

            ItemSpacer(25.dp)
            PasswordEditText(
                value = wifiUiState.password,
                onValueChange = onPasswordUpdated,
                modifier = modifier.fillMaxWidth()
            )
        }

        ItemSpacer(35.dp)
        Header1Text(
            text = stringResource(id = R.string.kWifiViewControllerSelectSecurityLabelText),
            modifier = modifier)

        ItemSpacer(25.dp)
        TextRectangularButton(
            onClick = { onSecurityButtonPressed(navController) },
            modifier = modifier.wrapContentWidth(),
            text = stringResource(id = wifiUiState.wifiType.stringValueResId)
        )

        ItemSpacer(35.dp)
        TextRectangularButton(
            onClick = { onFinishButtonPressed(navController)},
            modifier = modifier.wrapContentWidth(),
            enabled = wifiUiState.isPasswordValid,
            text = stringResource(id = R.string.kCreateWifiCodeButtonText)
        )

    }
}