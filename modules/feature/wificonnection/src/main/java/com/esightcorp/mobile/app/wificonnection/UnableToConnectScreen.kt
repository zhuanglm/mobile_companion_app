package com.esightcorp.mobile.app.wificonnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.state.UnableToConnectUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.UnableToConnectViewModel

@Composable
fun UnableToConnectRoute(
    navController: NavController,
    vm: UnableToConnectViewModel = hiltViewModel()
){
    val uiState by vm.uiState.collectAsState()
    UnableToConnectScreen(navController = navController, vm = vm, uiState = uiState)
}

@Composable
internal fun UnableToConnectScreen(
    navController: NavController,
    vm: UnableToConnectViewModel,
    modifier: Modifier = Modifier,
    uiState: UnableToConnectUiState
) {
    val TAG = "UnableToConnectScreen"

}