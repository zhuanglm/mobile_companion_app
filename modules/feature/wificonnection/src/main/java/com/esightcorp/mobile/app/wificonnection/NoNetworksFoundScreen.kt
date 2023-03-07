package com.esightcorp.mobile.app.wificonnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.state.NoNetworksFoundUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.NoNetworksFoundViewModel

@Composable
fun NoNetworksFoundRoute(
    navController: NavController,
    vm: NoNetworksFoundViewModel = hiltViewModel()
){
    val uiState by vm.uiState.collectAsState()
    NoNetworksFoundScreen(navController = navController, vm = vm, modifier = Modifier, uiState = uiState)
}

@Composable
internal fun NoNetworksFoundScreen(
    navController: NavController,
    vm: NoNetworksFoundViewModel,
    modifier: Modifier = Modifier,
    uiState: NoNetworksFoundUiState
) {
    val TAG = "NoNetworksFoundScreen"

}