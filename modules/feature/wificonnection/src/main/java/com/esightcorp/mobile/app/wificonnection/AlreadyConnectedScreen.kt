package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.wificonnection.state.AlreadyConnectedUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.AlreadyConnectedViewModel
import com.esightcorp.mobile.app.ui.R

@Composable
fun AlreadyConnectedRoute(
    navController: NavController,
    vm: AlreadyConnectedViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    AlreadyConnectedScreen(
        navController = navController, vm = vm, modifier = Modifier, uiState = uiState
    )
}


@Composable
internal fun AlreadyConnectedScreen(
    navController: NavController,
    vm: AlreadyConnectedViewModel,
    modifier: Modifier = Modifier,
    uiState: AlreadyConnectedUiState,
    onBackPressed: () -> Unit = { navController.popBackStack() },
) {
    val TAG = "AlreadyConnectedScreen"
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, headerText, header2Text, helpText, button) = createRefs()
            ESightTopAppBar(showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackPressed,
                onSettingsButtonInvoked = { /*Not shown*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header1Text(text = stringResource(id =R.string.wifi_already_connected_header), modifier = modifier.constrainAs(headerText) {
                top.linkTo(topBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Subheader(text = stringResource(id = R.string.wifi_already_connected_body),
                modifier = modifier.constrainAs(header2Text) {
                    top.linkTo(headerText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header1Text(text = stringResource(id = R.string.wifi_already_connected_action),
                modifier = modifier.constrainAs(helpText) {
                    top.linkTo(header2Text.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            TextRectangularButton(
                onClick = { Log.i(TAG, "AlreadyConnectedScreen: ") },
                modifier = modifier,
                text = stringResource(id = R.string.wifi_already_connected_button),
            )
        }

    }
}