package com.esightcorp.mobile.app.eshare

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectedViewModel
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.eshare.AutoFitTextureView
import com.esightcorp.mobile.app.ui.components.eshare.RotateToLandscape
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import com.esightcorp.mobile.app.utils.eShareConnectionStatus

@Composable
fun EshareConnectedRoute(
    navController: NavController,
    vm: EshareConnectedViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    eShareConnectedScreen(
        vm = vm,
        uiState = uiState,
        navController = navController,
        navigateToStoppedRoute = vm::navigateToStoppedRoute,
        navigateToUnableToConnectRoute = vm::navigateToUnableToConnectRoute,
        navigateToBusyRoute = vm::navigateToBusyRoute

    )
}

private const val TAG = "EshareConnectedRoute"

@Composable
fun eShareConnectedScreen(
    vm: EshareConnectedViewModel,
    uiState: EshareConnectedUiState,
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToStoppedRoute: (NavController) -> Unit,
    navigateToUnableToConnectRoute: (NavController) -> Unit,
    navigateToBusyRoute: (NavController) -> Unit
) {
    Log.i(TAG, "eShareConnectedScreen: ")
    Row {
        AndroidView(factory = { context ->
            AutoFitTextureView(context).apply {
                surfaceTextureListener = vm
            }
        }, modifier = modifier
            .width(1920.dp)
            .height(1080.dp), update = { view ->
            view.surfaceTextureListener = vm
            Log.i(TAG, "eShareConnectedScreen: TextureView updated ${view.isAvailable}")

        })
    }



    when (uiState.connectionState) {

        eShareConnectionStatus.Connected -> {
            Log.i(TAG, "eShareConnectedScreen: We are now connected to HMD ")
            RotateToLandscape()

        }

        eShareConnectionStatus.Initiated -> {
            LoadingScreenWithSpinner(loadingText = "Connection initiated",
                modifier = modifier,
                cancelButtonNeeded = true,
                onCancelButtonClicked = {
                    navController.popBackStack("home_first", false)
                })
        }

        eShareConnectionStatus.Disconnected -> {
            Log.i(TAG, "eShareConnectedScreen: We are now disconnected from HMD ")
            LoadingScreenWithIcon(
                loadingText = "Connection lost",
                modifier = modifier
            )
            navigateToStoppedRoute(navController)
        }

        eShareConnectionStatus.IP_NOT_REACHABLE -> {
            Log.i(TAG, "eShareConnectedScreen: IP not reachable")
            LaunchedEffect(Unit){
                navigateToUnableToConnectRoute(navController)
            }
        }

        eShareConnectionStatus.ADDR_NOT_AVAILABLE -> {
            Log.i(TAG, "eShareConnectedScreen: Address not available")
            LaunchedEffect(Unit){
                navigateToUnableToConnectRoute(navController)
            }
        }

        eShareConnectionStatus.BUSY -> {
            Log.i(TAG, "eShareConnectedScreen: HMD has a session running already")
            navigateToBusyRoute(navController)
        }

        else -> {
            Log.e(TAG, "eShareConnectedScreen: Should not hit here")
        }
    }


}

