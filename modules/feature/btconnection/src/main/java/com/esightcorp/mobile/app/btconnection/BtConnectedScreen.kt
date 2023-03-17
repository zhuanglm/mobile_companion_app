package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.viewmodels.BtConnectedViewModel
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import kotlinx.coroutines.delay
import com.esightcorp.mobile.app.ui.R
@Composable
fun BtConnectedRoute(
    navController: NavController,
    vm: BtConnectedViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    if(!uiState.isBtEnabled){
        NavigateBluetoothDisabled(navController = navController)
    }else{
        BtConnectedScreen(
            navController = navController,
            modifier = Modifier,
            deviceAddress = uiState.deviceAddress,
            deviceName = uiState.deviceName
        )
    }

}

@Composable
internal fun BtConnectedScreen(
    navController: NavController, modifier: Modifier, deviceAddress: String?, deviceName: String?
) {
    val homeRoute = stringResource(id = R.string.navigate_to_home) + "{$deviceName}"
    val screenTimeout = 5000L //5s in milliseconds
    if (deviceName != null && deviceAddress != null) {
        val loadingText = stringResource(id = R.string.connected_to) + " ${deviceName}\n" + "Address: $deviceAddress"
        LoadingScreenWithIcon(modifier = modifier, loadingText = loadingText)
    }else{
        val loadingText = stringResource(id = R.string.something_went_wrong)
        LoadingScreenWithIcon(modifier = modifier, loadingText = loadingText)
    }
    LaunchedEffect(Unit) {
        delay(screenTimeout)
        navController.navigate(homeRoute)
    }
}