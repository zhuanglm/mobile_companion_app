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
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.btconnection.state.BtSearchingUiState
import com.esightcorp.mobile.app.btconnection.viewmodels.BtSearchingViewModel
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.ui.R

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

    when (uiState.isScanning) {
        ScanningStatus.Failed -> {
            Log.e(TAG, "Bluetooth Scanning has failed. Show the error screen")
        }
        ScanningStatus.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(BtConnectionScreens.BtDevicesScreen.route)
            }
        }
        else -> {
            LoadingScreenWithSpinner(
                loadingText = stringResource(id = R.string.bt_searching_text),
                modifier = modifier
            )
            if (uiState.isScanning == ScanningStatus.Unknown) {
                vm.triggerScan()
            }
        }
    }


}

