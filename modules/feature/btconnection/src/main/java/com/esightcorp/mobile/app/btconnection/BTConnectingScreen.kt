package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.state.BtConnectingUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectingViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner

@Composable
fun BtConnectingRoute(
    navController: NavController,
    vm: BtConnectingViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    if (uiState.isBtEnabled) {
        BtConnectingScreen(
            modifier = Modifier,
            navController = navController,
            uiState = uiState,
            vm = vm,
        )
    } else {
        NavigateBluetoothDisabled(navController = navController)
    }
}

//region Internal implementation
private const val TAG = "BtConnectingScreen"

@Composable
private fun BtConnectingScreen(
    modifier: Modifier,
    navController: NavController,
    uiState: BtConnectingUiState,
    vm: BtConnectingViewModel
) {
    if (uiState.deviceAddress != null && uiState.deviceName != null) {
        if (uiState.didDeviceConnect) {
            LaunchedEffect(Unit) {
                Log.i(TAG, "${uiState.deviceName} is connected")
                vm.navigateToConnectedScreen(navController)
            }
        } else {
            LaunchedEffect(Unit) {
                Log.e(TAG, "BtConnectingScreen: We did not connect, go to error screen")
                vm.navigateToUnableToConnectScreen(navController)
            }
        }
    } else {
        LoadingScreenWithSpinner(
            loadingText = stringResource(R.string.kBTConnectSpinnerTitle), modifier = modifier
        )
    }
}
//endregion
