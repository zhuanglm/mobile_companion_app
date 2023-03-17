package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.*
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToConnectButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.wificonnection.state.UnableToConnectUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.UnableToConnectViewModel

@Composable
fun UnableToConnectRoute(
    navController: NavController, vm: UnableToConnectViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    vm.setNavController(navController)
    UnableToConnectScreen(
        navController = navController,
        vm = vm,
        uiState = uiState,
        onBackPressed = vm::onBackPressed,
        onTryAgain = vm::onTryAgain)
}

@Composable
internal fun UnableToConnectScreen(
    navController: NavController,
    vm: UnableToConnectViewModel,
    modifier: Modifier = Modifier,
    uiState: UnableToConnectUiState,
    onBackPressed: () -> Unit,
    onTryAgain: () -> Unit
) {
    val TAG = "UnableToConnectScreen"
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, headerText, header2Text, nhiOne, nhiTwo, nhiThree, helpText, howToConnect, button) = createRefs()
            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackPressed,
                onSettingsButtonInvoked = { /*Unused, not shown*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header1Text(
                text = stringResource(id = R.string.wifi_unable_to_connect_header),
                modifier = modifier.constrainAs(headerText) {
                    top.linkTo(topBar.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                })

            Header2Text(text = stringResource(id = R.string.wifi_unable_to_connect_subtitle),
                modifier = modifier.constrainAs(header2Text) {
                    top.linkTo(headerText.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                })

            NumberedHelpItem(number = 1,
                text = stringResource(id = R.string.wifi_unable_to_connect_1),
                modifier = modifier.constrainAs(nhiOne) {
                    top.linkTo(header2Text.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                })

            NumberedHelpItem(number = 2,
                text = stringResource(id = R.string.wifi_unable_to_connect_2),
                modifier = modifier.constrainAs(nhiTwo) {
                    top.linkTo(nhiOne.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                })

            NumberedHelpItem(number = 3,
                text = stringResource(id = R.string.wifi_unable_to_connect_3),
                modifier = modifier.constrainAs(nhiThree) {
                    top.linkTo(nhiTwo.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                })
            TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(button) {
                top.linkTo(nhiThree.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }, text = stringResource(id = R.string.wifi_unable_to_connect_button))

            BodyText(text = "",
                modifier = modifier.constrainAs(helpText) {
                })
            HowToConnectButton(modifier = modifier.constrainAs(howToConnect) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }) {
                //todo: This should be a deeplink to the web
                navController.navigate("how_to_connect")
            }
        }
    }

}