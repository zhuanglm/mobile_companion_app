/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.IconAndTextRectangularButton
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
    } else {
        if (uiState.networkList.isEmpty()) {
            LaunchedEffect(Unit) {
                vm.navigateToNoNetworksFoundScreen(navController)
            }
        } else {
            SelectNetworkScreen(
                modifier = Modifier,
                navController = navController,
                onBackButtonClicked = vm::onBackButtonClicked,
                onAdvancedButtonClicked = vm::onAdvancedButtonClicked,
                uiState = uiState,
                vm = vm
            )
        }

    }


}

private const val TAG = "SelectNetworkScreen"

//TODO: String resources
@Composable
internal fun SelectNetworkScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: (NavController) -> Unit,
    onAdvancedButtonClicked: (NavController) -> Unit,
    navController: NavController,
    uiState: SelectNetworkUiState,
    vm: SelectNetworkViewModel

) {
    val dummyNetworkList = listOf<String>(
        "Home Wi-Fi",
        "Office Wi-Fi",
        "It burns when IP",
        "FBI Mobile Unit",
        "VIRUS.EXE"
    )

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
    }
    HomeBaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackButtonClicked(navController) },
        onSettingsButtonInvoked = { /*Unused*/ },
        bottomButton = {
            AdvancedSettingsButton(
                modifier = modifier,
                onAdvancedSettingsClick = { onAdvancedButtonClicked(navController) }
            )
        }) {

        SelectNetworkBody(
            modifier = modifier,
            navController = navController,
            uiState = uiState,
            vm = vm
        )

    }

}

@Composable
private fun SelectNetworkBody(
    modifier: Modifier,
    navController: NavController,
    uiState: SelectNetworkUiState,
    vm: SelectNetworkViewModel
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, networkContainer) = createRefs()
        Header1Text(
            text = stringResource(id = R.string.kWifiViewSelectWifiNameLabelText),
            modifier = modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 35.dp)
                    start.linkTo(parent.start)
                })
        LazyColumn(modifier = modifier
            .constrainAs(networkContainer) {
                top.linkTo(header.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }) {
            items(uiState.networkList) { network ->
                IconAndTextRectangularButton(
                    onClick = {
                        vm.selectNetwork(network)
                        vm.navigateToPasswordScreen(navController)
                    },
                    modifier = Modifier,
                    icon = ImageVector.vectorResource(id = R.drawable.round_wifi_24),
                    text = network.SSID
                )
                Spacer(modifier = modifier.padding(10.dp))

            }

        }
    }

}