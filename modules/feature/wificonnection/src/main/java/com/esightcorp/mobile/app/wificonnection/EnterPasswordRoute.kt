/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.AdvancedSettingsButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.PasswordEditText
import com.esightcorp.mobile.app.wificonnection.viewmodels.EnterPasswordViewModel

@Composable
fun EnterPasswordRoute(
    navController: NavController,
    viewModel: EnterPasswordViewModel = hiltViewModel()
) {
    Log.d(TAG, "EnterPasswordRoute:")

    val isConnected by viewModel.connectionStateFlow().collectAsState()
    if (isConnected == false) {
        LaunchedEffect(Unit) { viewModel.onBleDisconnected(navController) }
        return
    }

    EnterPasswordScreen(
        navController = navController,
        vwModel = viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun EnterPasswordScreenPreview() = MaterialTheme {
    EnterPasswordScreen(navController = rememberNavController())
}

//region Private implementation
private const val TAG = "WifiCredentialsRoute"

@Composable
internal fun EnterPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vwModel: EnterPasswordViewModel? = null
) = PasswordField(
    modifier = modifier,
    navController = navController,
    vwModel = vwModel
)

@Composable
private fun PasswordField(
    modifier: Modifier,
    navController: NavController,
    vwModel: EnterPasswordViewModel? = null
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {}

    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { vwModel?.onBackButtonPressed(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = {
            AdvancedSettingsButton(
                modifier = modifier,
                onAdvancedSettingsClick = { vwModel?.onAdvancedButtonPressed(navController) },
            )
        },
    ) {
        PasswordBody(
            modifier = modifier,
            navController = navController,
            vwModel = vwModel
        )
    }
}

@Composable
private fun PasswordBody(
    modifier: Modifier,
    navController: NavController,
    vwModel: EnterPasswordViewModel? = null
) {
    ConstraintLayout {
        val (header, editText, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.kWifiViewWifiPasswordLabelText),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
            },
        )

        val wifiUiState = vwModel?.uiState?.collectAsState()?.value

        PasswordEditText(
            value = wifiUiState?.password ?: "",
            onValueChange = { vwModel?.updatePassword(it) },
            modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(header.bottom, margin = 25.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(),
        )

        TextRectangularButton(
            onClick = { vwModel?.wifiPasswordSubmitted(navController) },
            modifier = modifier.constrainAs(button) {
                top.linkTo(editText.bottom, margin = 25.dp)
                start.linkTo(editText.start)
                end.linkTo(editText.end)
            },
            text = stringResource(R.string.kWifiViewConnectButtonText),
            enabled = wifiUiState?.isPasswordValid ?: false
        )
    }
}

//endregion
