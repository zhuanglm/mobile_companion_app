package com.esightcorp.mobile.app.wificonnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.state.AlreadyConnectedUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.AlreadyConnectedViewModel

@Composable
fun AlreadyConnectedRoute(
    navController: NavController,
    vm: AlreadyConnectedViewModel = hiltViewModel(),
){
    val uiState by vm.uiState.collectAsState()
    AlreadyConnectedScreen(navController = navController, vm = vm, modifier = Modifier, uiState = uiState)
}


@Composable
internal fun AlreadyConnectedScreen(
    navController: NavController,
    vm: AlreadyConnectedViewModel,
    modifier: Modifier = Modifier,
    uiState: AlreadyConnectedUiState
) {
    val TAG = "AlreadyConnectedScreen"

}