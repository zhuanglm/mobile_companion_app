package com.esightcorp.mobile.app.wificonnection

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.utils.ScanningStatus
import com.esightcorp.mobile.app.wificonnection.state.WifiSearchingUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiSearchingViewModel

@Composable
fun SearchingForNetworksRoute(
    navController: NavController,
    flow: String?,
    vm: WifiSearchingViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    Log.d("TAG", "SearchingForNetworksRoute: {$flow}")
    LaunchedEffect(Unit ) {
        vm.setWifiFlow(flow)
    }

    if (!uiState.isWifiEnabled) {
        NavigateToWifiOffScreen(navController = navController)
    } else {
        if (flow == WifiNavigation.ScanningRoute.PARAM_WIFI_CONNECTION) {
            if (uiState.isWifiConnected) {
                vm.navigateToWifiAlreadyConnected(navController)
            } else {
                LaunchedEffect(Unit ) {
                    vm.setWifiFlow(WifiNavigation.ScanningRoute.PARAM_BLUETOOTH)
                }
                Log.d("TAG", "Set flow to bluetooth")
            }
        }

        Log.d("TAG", "SearchingForNetworksRoute: WIFI ENABLED")
        SearchingForNetworksScreen(
            modifier = Modifier,
            navController = navController,
            vm = vm,
            uiState = uiState,
            loadingText = stringResource(id = com.esightcorp.mobile.app.ui.R.string.kWifiSearchSpinnerTitle))
    }

}

@Composable
internal fun SearchingForNetworksScreen(
    modifier: Modifier = Modifier,
    loadingText: String = "Searching for Wi-Fi networks",
    navController: NavController,
    vm: WifiSearchingViewModel,
    uiState: WifiSearchingUiState
) {
    when(uiState.scanningStatus){
        ScanningStatus.Failed -> {
            Log.e("SearchingForNetworksScreen", "SearchingForNetworksScreen: SCAN STATUS FAILED")
            //TODO: implement this!
        }
        ScanningStatus.Success -> {
            Log.d("TAG", "SearchingForNetworksScreen: SUCCESS")
            LaunchedEffect(Unit){
                vm.navigateToWifiNetworksScreen(navController)
            }
        }
        else -> {
            Log.i("SearchingForNetworksScreen", "Searching for networks...")
            Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
                LoadingScreenWithSpinner(loadingText = loadingText, modifier = modifier)
            }
        }
    }

}
