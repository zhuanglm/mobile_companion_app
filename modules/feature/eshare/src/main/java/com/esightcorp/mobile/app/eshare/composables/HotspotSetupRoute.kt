package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.esightcorp.mobile.app.eshare.state.HotspotSetupUiState
import com.esightcorp.mobile.app.eshare.viewmodels.HotspotSetupViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.UnableToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback

@Composable
fun HotspotSetupRoute(
    navController: NavController,
    vm: HotspotSetupViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    HotspotSetupScreen(
        navController = navController,
        onBackPressed = vm::gotoMainScreen,
        modifier = Modifier,
        uiState = uiState
    )
}

@Preview
@Composable
fun HotspotSetupScreenPreview() = MaterialTheme {
    HotspotSetupScreen(
        navController = rememberNavController(),
        uiState = HotspotSetupUiState(networkName = "AAA", networkPassword = "123"),
    )
}

//region Internal implementation

@Composable
internal fun HotspotSetupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBackPressed: OnNavigationCallback? = null,
    onShareViewPressed: OnNavigationCallback? = null,
    onUnableToConnectPressed: OnNavigationCallback? = null,
    uiState: HotspotSetupUiState
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = {
            UnableToConnectButton { onUnableToConnectPressed?.invoke(navController) }
        },
    ) {
        if (uiState.networkName != "" && uiState.networkPassword != "") {
            HotspotSetupBody(
                modifier = modifier,
                navController = navController,
                onShareViewPressed = onShareViewPressed,
                uiState = uiState
            )
        }
    }
}

@Composable
private fun HotspotSetupBody(
    modifier: Modifier,
    navController: NavController,
    onShareViewPressed: OnNavigationCallback? = null,
    uiState: HotspotSetupUiState
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subtitle, help1, help2, help3, help4, button) = createRefs()

        Header1Text(
            text = stringResource(id = R.string.hotspot_setup_header),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 25.dp)
                start.linkTo(parent.start)
            },
        )

        Subheader(
            text = stringResource(id = R.string.hotspot_setup_subheader),
            modifier = modifier.constrainAs(subtitle) {
                top.linkTo(header.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            },
        )

        NumberedHelpItem(
            number = 1,
            text = stringResource(id = R.string.hotspot_setup_step_1),
            modifier = modifier.constrainAs(help1) {
                top.linkTo(subtitle.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        NumberedHelpItem(
            number = 2,
            text = stringResource(id = R.string.hotspot_setup_step_2, uiState.networkName),
            modifier = modifier.constrainAs(help2) {
                top.linkTo(help1.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        NumberedHelpItem(
            number = 3,
            text = stringResource(id = R.string.hotspot_setup_step_3, uiState.networkPassword),
            modifier = modifier.constrainAs(help3) {
                top.linkTo(help2.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        NumberedHelpItem(
            number = 4,
            text = stringResource(id = R.string.hotspot_setup_step_4),
            modifier = modifier.constrainAs(help4) {
                top.linkTo(help3.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        TextRectangularButton(
            onClick = { onShareViewPressed?.invoke(navController) },
            modifier = modifier.constrainAs(button) {
                top.linkTo(help4.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(id = R.string.bt_no_devices_found_button)
        )
    }
}
//endregion
