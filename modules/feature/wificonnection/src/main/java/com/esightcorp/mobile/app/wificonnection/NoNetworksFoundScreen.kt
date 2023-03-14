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
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Header2Text
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.help.NumberedHelpItem
import com.esightcorp.mobile.app.wificonnection.state.NoNetworksFoundUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.NoNetworksFoundViewModel
import com.esightcorp.mobile.app.ui.R

@Composable
fun NoNetworksFoundRoute(
    navController: NavController,
    vm: NoNetworksFoundViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    NoNetworksFoundScreen(
        navController = navController,
        vm = vm,
        modifier = Modifier,
        uiState = uiState
    )
}

@Composable
internal fun NoNetworksFoundScreen(
    navController: NavController,
    vm: NoNetworksFoundViewModel,
    modifier: Modifier = Modifier,
    uiState: NoNetworksFoundUiState,
    onBackButton: () -> Unit = { navController.popBackStack() },
) {
    val TAG = "NoNetworksFoundScreen"
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, headerText, header2Text, nhiOne, nhiTwo, button) = createRefs()

            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButton,
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Header1Text(
                text = stringResource(id = R.string.wifi_no_networks_header),
                modifier = modifier.constrainAs(headerText) {
                    top.linkTo(topBar.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

            Header2Text(
                text = stringResource(id = R.string.wifi_no_networks_subtitle),
                modifier = modifier.constrainAs(header2Text) {
                    top.linkTo(headerText.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

            NumberedHelpItem(
                number = 1,
                text = stringResource(id = R.string.wifi_no_networks_1),
                modifier = modifier.constrainAs(nhiOne) {
                    top.linkTo(header2Text.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )
            NumberedHelpItem(
                number = 2,
                text = stringResource(id = R.string.wifi_no_networks_2),
                modifier = modifier.constrainAs(nhiTwo) {
                    top.linkTo(nhiOne.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )
            TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(button){
                top.linkTo(nhiTwo.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, text = stringResource(id = R.string.wifi_no_networks_button) )
        }
    }

}