package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.wificonnection.viewmodels.NoNetworksFoundViewModel

@Composable
fun NoNetworksFoundRoute(
    navController: NavController,
    vm: NoNetworksFoundViewModel = hiltViewModel()
) {
    vm.setNavController(navController)
    NoNetworksFoundScreen(
        modifier = Modifier,
        onBackPressed = vm::navigateHome,
        onTryAgainClicked = vm::tryAgain,
        onHelpClicked = vm::showHowToConnectPage,
    )
}

@Preview
@Composable
private fun NoNetworksFoundScreenPreview() = MaterialTheme {
    NoNetworksFoundScreen(
        modifier = Modifier,
        onBackPressed = {},
        onTryAgainClicked = {},
        onHelpClicked = {})
}

@Composable
internal fun NoNetworksFoundScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onTryAgainClicked: () -> Unit,
    onHelpClicked: () -> Unit,
) {
    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = onBackPressed,
        onSettingsButtonInvoked = { },
        bottomButton = {
            CantFindDeviceButton(
                modifier.padding(top = dimensionResource(R.dimen.bottom_button_top_padding)),
                R.string.kWifiTroubleshootingNoWifiFooterButtonTitle,
                onHelpClicked
            )
        },
    ) {
        NoNetworksFoundScreenBody(
            modifier = modifier,
            onTryAgainClicked = onTryAgainClicked
        )
    }
}

@Composable
private fun NoNetworksFoundScreenBody(
    modifier: Modifier = Modifier,
    onTryAgainClicked: OnActionCallback,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (headerText, header2Text, nhiOne, nhiTwo, button) = createRefs()

        Header1Text(text = stringResource(id = R.string.kWifiTroubleshootingNoWifiTitle),
            modifier = modifier
                .constrainAs(headerText) {
                    top.linkTo(parent.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

        BodyText(
            text = stringResource(id = R.string.kTroubleShootingTryFollowingSteps),
            modifier = modifier
                .constrainAs(header2Text) {
                    top.linkTo(headerText.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
            color = MaterialTheme.colors.onSurface
        )

        NumberedHelpItem(number = 1,
            text = stringResource(id = R.string.kTroubleshootingInstructionRestartESightDevice),
            modifier = modifier
                .constrainAs(nhiOne) {
                    top.linkTo(header2Text.bottom, margin = 36.dp)
                    start.linkTo(parent.start)
                })
        NumberedHelpItem(number = 2,
            text = stringResource(id = R.string.kTroubleshootingInstructionRestartRouter),
            modifier = modifier
                .constrainAs(nhiTwo) {
                    top.linkTo(nhiOne.bottom, margin = 36.dp)
                    start.linkTo(parent.start)
                })
        TextRectangularButton(
            onClick = onTryAgainClicked,
            modifier = modifier
                .constrainAs(button) {
                    top.linkTo(nhiTwo.bottom, margin = 36.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = stringResource(id = R.string.kTryAgainButtonTitle)
        )
    }
}
