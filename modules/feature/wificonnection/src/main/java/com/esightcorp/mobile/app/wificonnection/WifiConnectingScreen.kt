package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.wificonnection.state.WifiConnectingUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectingViewModel

@Composable
fun WifiConnectingRoute(
    navController: NavController, vm: WifiConnectingViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    WifiConnectingScreen(navController = navController, vm = vm, modifier = Modifier, uiState = uiState)

}

@Composable
internal fun WifiConnectingScreen(
    navController: NavController, vm: WifiConnectingViewModel, modifier: Modifier = Modifier, uiState: WifiConnectingUiState
) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout() {
            val loading = createRef()
            LoadingScreenWithSpinner(
                modifier = modifier.constrainAs(loading) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, loadingText = "Connecting to ${uiState.ssid}"
            )
        }

    }

}