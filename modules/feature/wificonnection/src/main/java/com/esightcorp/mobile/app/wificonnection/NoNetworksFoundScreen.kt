package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindWifiButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.ui.components.text.BodyText
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.wificonnection.viewmodels.NoNetworksFoundViewModel

@Composable
fun NoNetworksFoundRoute(
    navController: NavController, vm: NoNetworksFoundViewModel = hiltViewModel()
) {
    vm.setNavController(navController)
    NoNetworksFoundScreen(
        modifier = Modifier,
        onTryAgain = vm::tryAgain,
        onBackPressed = vm::navigateHome,
        onHelpClicked = vm::showHowToConnectPage,
    )
}

@Preview
@Composable
private fun NoNetworksFoundScreenPreview() = MaterialTheme {
    NoNetworksFoundScreen(
        modifier = Modifier,
        onTryAgain = { } ,
        onBackPressed = { },
        onHelpClicked = {})
}

@Composable
internal fun NoNetworksFoundScreen(
    //navController: NavController,
    //vm: NoNetworksFoundViewModel,
    modifier: Modifier = Modifier,
    //uiState: NoNetworksFoundUiState,
    onBackPressed: () -> Unit,
    onTryAgain: () -> Unit,
    onHelpClicked: () -> Unit
) {
    //val TAG = "NoNetworksFoundScreen"
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, headerText, header2Text, nhiOne, nhiTwo, button, help) = createRefs()

            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackPressed,
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header1Text(text = stringResource(id = R.string.kWifiTroubleshootingNoWifiTitle),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(headerText) {
                        top.linkTo(topBar.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    })

            BodyText(text = stringResource(id = R.string.kTroubleShootingTryFollowingSteps),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(header2Text) {
                        top.linkTo(headerText.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    },
                color = MaterialTheme.colors.onSurface)

            NumberedHelpItem(number = 1,
                text = stringResource(id = R.string.kTroubleshootingInstructionRestartESightDevice),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(nhiOne) {
                        top.linkTo(header2Text.bottom, margin = 36.dp)
                        start.linkTo(parent.start)
                    })
            NumberedHelpItem(number = 2,
                text = stringResource(id = R.string.kTroubleshootingInstructionRestartRouter),
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(nhiTwo) {
                        top.linkTo(nhiOne.bottom, margin = 36.dp)
                        start.linkTo(parent.start)
                    })
            TextRectangularButton(
                onClick = onTryAgain,
                modifier = modifier
                    .padding(25.dp, 0.dp, 25.dp, 0.dp)
                    .constrainAs(button) {
                        top.linkTo(nhiTwo.bottom, margin = 36.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.kTryAgainButtonTitle)
            )

            CantFindWifiButton(
                modifier = modifier
                    .padding(top = dimensionResource(R.dimen.bottom_button_top_padding))
                    .constrainAs(help) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onHelpClick = onHelpClicked
            )
        }
    }

}