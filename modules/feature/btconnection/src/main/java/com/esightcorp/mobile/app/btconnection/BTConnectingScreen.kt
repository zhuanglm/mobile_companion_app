package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BtConnectingUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectingViewModel
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner

@Composable
fun BtConnectingRoute(
    navController: NavController, vm: BtConnectingViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    BtConnectingScreen(
        modifier = Modifier, navController = navController, uiState = uiState, vm = vm
    )
}

@Composable
internal fun BtConnectingScreen(
    modifier: Modifier,
    navController: NavController,
    uiState: BtConnectingUiState,
    vm: BtConnectingViewModel
) {
    val TAG = "BtConnectingScreen"
    if (uiState.deviceAddress != null && uiState.deviceName != null) {
        if (uiState.didDeviceConnect) {
            LaunchedEffect(Unit){
                Log.i(TAG, "BtConnectingScreen: ${uiState.deviceName} is connected")
                vm.navigateToConnectedScreen(navController, uiState.deviceName, uiState.deviceAddress)
            }
        } else {
            Log.e(TAG, "BtConnectingScreen: Send me to the error page, we did not connect")
        }
    } else {
        LoadingScreenWithSpinner(
            loadingText = "Connecting to eSight", modifier = modifier
        )
    }

}
