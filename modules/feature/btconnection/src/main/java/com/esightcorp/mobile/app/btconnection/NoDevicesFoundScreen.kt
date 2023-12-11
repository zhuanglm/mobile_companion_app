package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
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
import com.esightcorp.mobile.app.btconnection.state.NoDevicesFoundUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.NoDevicesFoundViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem


@Composable
fun NoDevicesFoundRoute(
    navController: NavController,
    vm: NoDevicesFoundViewModel = hiltViewModel()
) {
    val btUiState by vm.uiState.collectAsState()
    if (btUiState.isBtEnabled) {
        NoDevicesFoundScreen(
            onBackButtonClicked = vm::navigateToNoDevicesConnectedScreen,
            onTryAgainClicked = vm::navigateToSearchingScreen,
            onBluetoothDisabled = vm::onBluetoothDisabled,
            onHelpClicked = vm::navigateToUnableToConnectScreen,
            uiState = btUiState,
            navController = navController
        )
    } else {
        NavigateBluetoothDisabled(navController = navController)
    }
}

@Preview
@Composable
fun NoDevicesFoundScreenPreview() = MaterialTheme {
    NoDevicesFoundScreen(
        onBackButtonClicked = {},
        onTryAgainClicked = {},
        onBluetoothDisabled = {},
        onHelpClicked = {},
        uiState = NoDevicesFoundUiState(isBtEnabled = true),
        navController = rememberNavController(),
    )
}

//region Internal implementation
private const val TAG = "NoDevicesFoundScreen"

@Composable
internal fun NoDevicesFoundScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: (NavController) -> Unit,
    onTryAgainClicked: (NavController) -> Unit,
    onBluetoothDisabled: (NavController) -> Unit,
    onHelpClicked: (NavController) -> Unit,
    uiState: NoDevicesFoundUiState,
    navController: NavController
) {
    Log.i(TAG, "NoDevicesFoundScreen: ")
    when (uiState.isBtEnabled) {
        false -> LaunchedEffect(Unit) { onBluetoothDisabled(navController) }
        true -> {
            BaseScreen(
                modifier = modifier,
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = { onBackButtonClicked(navController) },
                onSettingsButtonInvoked = { },
                bottomButton = {
                    CantFindDeviceButton(modifier, R.string.kUnableToConnectFooterButtonText) {
                        onHelpClicked.invoke(navController)
                    }
                },
            ) {
                NoDevicesFoundBody(
                    modifier = modifier,
                    onTryAgainClicked = { onTryAgainClicked(navController) },
                )
            }
        }
    }
}

@Composable
private fun NoDevicesFoundBody(
    modifier: Modifier,
    onTryAgainClicked: () -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subtitle, help1, help2, help3, button) = createRefs()

        Header1Text(
            text = stringResource(id = R.string.kBTTroubleshootingESightNotFoundTitle),
            modifier = modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 25.dp)
                    start.linkTo(parent.start)
                },
        )

        Subheader(
            text = stringResource(id = R.string.kTroubleShootingTryFollowingSteps),
            modifier = modifier
                .constrainAs(subtitle) {
                    top.linkTo(header.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
        )

        NumberedHelpItem(
            number = 1,
            text = stringResource(id = R.string.kTroubleshootingInstructionRestartESightDevice),
            modifier = modifier
                .constrainAs(help1) {
                    top.linkTo(subtitle.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        NumberedHelpItem(
            number = 2,
            text = stringResource(id = R.string.kTroubleshootingInstructionESightWithinRange),
            modifier = modifier
                .constrainAs(help2) {
                    top.linkTo(help1.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        NumberedHelpItem(
            number = 3,
            text = stringResource(id = R.string.kTroubleshootingInstructionSufficientCharge),
            modifier = modifier
                .constrainAs(help3) {
                    top.linkTo(help2.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        TextRectangularButton(
            onClick = onTryAgainClicked,
            modifier = modifier
                .constrainAs(button) {
                    top.linkTo(help3.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = stringResource(id = R.string.kTryAgainButtonTitle)
        )
    }
}
//endregion
