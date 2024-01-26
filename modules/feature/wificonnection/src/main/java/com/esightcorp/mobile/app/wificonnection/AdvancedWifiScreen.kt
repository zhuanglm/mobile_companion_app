/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.CustomEditText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.PasswordEditText
import com.esightcorp.mobile.app.wificonnection.state.WifiAdvancedSettingsUiState
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
        bottomButton = { Unit }) {

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

@Composable
private fun AdvancedWifiBody(
    modifier: Modifier,
    navController: NavController,
    wifiUiState: WifiAdvancedSettingsUiState,
    onSsidUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onSecurityButtonPressed: (NavController) -> Unit,
    onFinishButtonPressed: (NavController) -> Unit,

    ) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (ssidHeader, ssidField, pwdHeader, pwdField, securityHeader, securityButton, finishButton) = createRefs()

        Header1Text(
            text = stringResource(id = R.string.kWifiViewWifiNameLabelText),
            modifier = modifier.constrainAs(ssidHeader) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })

        CustomEditText(value = wifiUiState.ssid,
            onValueChange = onSsidUpdated,
            modifier = modifier.constrainAs(ssidField) {
                top.linkTo(ssidHeader.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.fillMaxWidth())
        if(!wifiUiState.wifiType.equals("none", ignoreCase = false)){
            Header1Text(
                text = stringResource(id = R.string.kWifiViewWifiPasswordLabelText),
                modifier = modifier.constrainAs(pwdHeader) {
                    top.linkTo(ssidField.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                })
            PasswordEditText(
                value = wifiUiState.password,
                onValueChange = onPasswordUpdated,
                modifier = modifier.constrainAs(pwdField) {
                    top.linkTo(pwdHeader.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.fillMaxWidth())
        }

        Header1Text(
            text = stringResource(id = R.string.kWifiViewControllerSelectSecurityLabelText),
            modifier = modifier.constrainAs(securityHeader) {
                top.linkTo(pwdField.bottom, margin = 35.dp)
                start.linkTo(parent.start)
            })

        TextRectangularButton(
            onClick = { onSecurityButtonPressed(navController) },
            modifier = modifier.constrainAs(securityButton) {
                top.linkTo(securityHeader.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = wifiUiState.wifiType
        )

        TextRectangularButton(
            onClick = { onFinishButtonPressed(navController)},
            modifier = modifier.constrainAs(finishButton) {
                top.linkTo(securityButton.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = false,
            text = stringResource(id = R.string.kCreateWifiCodeButtonText)
        )

    }

}