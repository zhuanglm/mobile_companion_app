package com.esightcorp.mobile.app.btconnection

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.state.BtSearchingUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtSearchingViewModel
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.utils.ScanningStatus
import javax.security.auth.login.LoginException

@Composable
fun BtSearchingRoute(
    navController: NavController,
    vm: BtSearchingViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    BtSearchingScreen(
        modifier = Modifier,
        navController = navController,
        uiState = uiState,
        vm = vm
    )
}

@Composable
internal fun BtSearchingScreen(
    modifier: Modifier,
    navController: NavController,
    uiState: BtSearchingUiState,
    vm: BtSearchingViewModel
) {
    val TAG = "BtSearchingScreen"

    when(uiState.isScanning){
        ScanningStatus.Failed -> {
            Log.i(TAG, "Bluetooth Scanning has failed. Show the error screen")
        }
        ScanningStatus.Success -> {
            LaunchedEffect(Unit){
                navController.navigate(BtConnectionScreens.BtDevicesScreen.route)
            }
        }
        else -> {
            LoadingScreenWithSpinner(
                loadingText = "Searching for eSight devices",
                modifier = modifier
            )
            if (uiState.isScanning == ScanningStatus.Unknown){
                vm.triggerScan()
            }
        }
    }


}

