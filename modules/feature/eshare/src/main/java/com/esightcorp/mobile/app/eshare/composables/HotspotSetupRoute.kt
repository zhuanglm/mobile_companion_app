package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.esightcorp.mobile.app.ui.components.IconAndTextSquareButton
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.UnableToConnectButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
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
        onShareViewPressed = vm::onRetryPressed,
        onUnableToConnectPressed = vm::showHowToConnectPage,
        modifier = Modifier,
        uiState = uiState
    )
}

//region Internal implementation

@Composable
private fun HotspotSetupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBackPressed: OnNavigationCallback? = null,
    onShareViewPressed: OnNavigationCallback? = null,
    onUnableToConnectPressed: OnActionCallback? = null,
    uiState: HotspotSetupUiState
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = { UnableToConnectButton { onUnableToConnectPressed?.invoke() } },
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
        val (header, subtitle, instruction, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.label_eshare_hotspot_setup_header),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top)
            },
        )

        Subheader(
            text = stringResource(R.string.label_eshare_hotspot_setup_sub_header),
            modifier = modifier.constrainAs(subtitle) {
                top.linkTo(header.bottom, margin = 10.dp)
            },
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.constrainAs(instruction) {
                top.linkTo(subtitle.bottom, margin = 10.dp)
            }
        ) {
            NumberedHelpItem(
                number = 1,
                text = stringResource(R.string.label_eshare_hotspot_setup_step_1),
            )
            ItemSpacer()

            NumberedHelpItem(
                number = 2,
                text = stringResource(
                    R.string.label_eshare_hotspot_setup_step_2,
                    uiState.networkName
                ),
            )
            ItemSpacer()

            NumberedHelpItem(
                number = 3,
                text = stringResource(
                    R.string.label_eshare_hotspot_setup_step_3,
                    uiState.networkPassword
                ),
            )
            ItemSpacer()

            NumberedHelpItem(
                number = 4,
                text = stringResource(R.string.label_eshare_hotspot_setup_step_4),
            )
            ItemSpacer()
        }

        IconAndTextSquareButton(
            text = stringResource(R.string.label_feature_eshare),
            painter = painterResource(R.drawable.baseline_camera_alt_24),
            onClick = { onShareViewPressed?.invoke(navController) },
            modifier = modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    top.linkTo(instruction.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(140.dp)
        )
    }
}

@Preview
@Composable
private fun HotspotSetupScreenPreview() = MaterialTheme {
    HotspotSetupScreen(
        navController = rememberNavController(),
        uiState = HotspotSetupUiState(networkName = "AAA", networkPassword = "123"),
    )
}
//endregion
