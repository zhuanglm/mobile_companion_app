package com.esightcorp.mobile.app.eshare.composables

import android.util.Log
import android.view.TextureView
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.eshare.AutoFitTextureView
import com.esightcorp.mobile.app.ui.components.eshare.RotateToLandscape
import com.esightcorp.mobile.app.utils.NavigateToBluetoothDisabled
import com.esightcorp.mobile.app.utils.NavigateToDeviceDisconnected
import com.esightcorp.mobile.app.utils.eShareConnectionStatus

@Composable
fun EshareConnectedRoute(
    navController: NavController,
    vm: EshareConnectedViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    if (!uiState.radioState.isBtEnabled){
        Log.i(TAG, "EshareConnectedRoute: Bluetooth is disabled right now")
        NavigateToBluetoothDisabled(navController = navController)
    }

    if(!uiState.radioState.isWifiEnabled){
        Log.i(TAG, "EshareConnectedRoute: Wifi is disabled right now")
    }

    if(!uiState.deviceConnectionState.isDeviceConnected){
        Log.i(TAG, "EshareConnectedRoute: Device is not connected")
        NavigateToDeviceDisconnected(navController = navController)
    }
    if(uiState.radioState.isBtEnabled && uiState.deviceConnectionState.isDeviceConnected){
        eShareConnectedScreen(
            textureViewListener = vm,
            uiState = uiState,
            navController = navController,
            navigateToStoppedRoute = vm::navigateToStoppedRoute,
            navigateToUnableToConnectRoute = vm::navigateToUnableToConnectRoute,
            navigateToBusyRoute = vm::navigateToBusyRoute,
            onCancelButtonClicked = vm::onCancelButtonClicked

        )
    }
    
}

private const val TAG = "EshareConnectedRoute"

@Composable
fun eShareConnectedScreen(
    textureViewListener: TextureView.SurfaceTextureListener,
    uiState: EshareConnectedUiState,
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToStoppedRoute: (NavController) -> Unit,
    navigateToUnableToConnectRoute: (NavController) -> Unit,
    navigateToBusyRoute: (NavController) -> Unit,
    onCancelButtonClicked: (NavController) -> Unit
) {
    Log.i(TAG, "eShareConnectedScreen: ")
    Row {
        AndroidView(factory = { context ->
            AutoFitTextureView(context).apply {
                surfaceTextureListener = textureViewListener
            }
        }, modifier = modifier
            .fillMaxHeight(), update = { view ->
            view.surfaceTextureListener = textureViewListener
            Log.i(TAG, "eShareConnectedScreen: TextureView updated ${view.isAvailable}")

        })
    }



    when (uiState.connectionState) {

        eShareConnectionStatus.Connected -> {
            Log.i(TAG, "eShareConnectedScreen: We are now connected to HMD ")
            RotateToLandscape()

        }

        eShareConnectionStatus.Initiated -> {
            LoadingScreenWithSpinner(loadingText = stringResource(R.string.eshare_loading_text),
                modifier = modifier,
                cancelButtonNeeded = true,
                onCancelButtonClicked = {
                    onCancelButtonClicked(navController)
                })
        }

        eShareConnectionStatus.Disconnected -> {
            Log.i(TAG, "eShareConnectedScreen: We are now disconnected from HMD ")
            LaunchedEffect(Unit) {
                navigateToStoppedRoute(navController)
            }
        }

        eShareConnectionStatus.IP_NOT_REACHABLE -> {
            Log.i(TAG, "eShareConnectedScreen: IP not reachable")
            LaunchedEffect(Unit) {
                navigateToUnableToConnectRoute(navController)
            }
        }

        eShareConnectionStatus.ADDR_NOT_AVAILABLE -> {
            Log.i(TAG, "eShareConnectedScreen: Address not available")
            LaunchedEffect(Unit) {
                navigateToUnableToConnectRoute(navController)
            }
        }

        eShareConnectionStatus.BUSY -> {
            Log.i(TAG, "eShareConnectedScreen: HMD has a session running already")
            LaunchedEffect(Unit) {
                navigateToBusyRoute(navController)
            }
        }

        else -> {
            Log.e(TAG, "eShareConnectedScreen: Should not hit here")
        }
    }


}

