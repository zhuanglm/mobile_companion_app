package com.esightcorp.mobile.app.eshare

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectedViewModel
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.eshare.AutoFitTextureView
import com.esightcorp.mobile.app.utils.eShareConnectionStatus
import java.io.InputStream

@Composable
fun EshareConnectedRoute(
    navController: NavController,
    vm: EshareConnectedViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    eShareConnectedScreen(vm = vm, uiState = uiState, navController = navController)
}

private const val TAG = "EshareConnectedRoute"
@Composable
fun eShareConnectedScreen(
    vm: EshareConnectedViewModel,
    uiState: EshareConnectedUiState,
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    AndroidView(factory = { context ->
        AutoFitTextureView(context).apply {
            surfaceTextureListener = vm

        }
    }, update = { view ->
        view.surfaceTextureListener = vm

    })
    when(uiState.connectionState){
        eShareConnectionStatus.Failed -> {
            TODO()
        }
        eShareConnectionStatus.Connected -> {
            Log.i(TAG, "eShareConnectedScreen: ")

        }
        eShareConnectionStatus.Disconnected -> TODO()
        eShareConnectionStatus.Initiated -> {
            LoadingScreenWithSpinner(loadingText = "Connecting HCS",
                modifier = modifier,
                cancelButtonNeeded = true,
                onCancelButtonClicked = {
                    navController.popBackStack("home", false)
                })
        }
        eShareConnectionStatus.ReceivedUserAcceptance -> TODO()
        eShareConnectionStatus.ReceivedUserRejection -> TODO()
        eShareConnectionStatus.Timeout -> TODO()
        eShareConnectionStatus.Unknown -> TODO()
    }


}