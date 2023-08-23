package com.esightcorp.mobile.app.eshare

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.navigation.EshareScreens
import com.esightcorp.mobile.app.eshare.state.EshareConnectingUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectingViewModel
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.utils.eShareConnectionStatus


private const val TAG = "EshareConnectingRoute"
//TODO: Can we scrap this route and only use the EshareConnectedRoute?
@Composable
fun EshareConnectingRoute(
    navController: NavController, vm: EshareConnectingViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    Log.i(TAG, "EshareConnectingRoute: uiState: ${uiState.radioState.isBtEnabled}")

    if (!uiState.radioState.isBtEnabled) {
        NavigateBluetoothDisabled(navController = navController)
    } else {
        EshareConnectingScreen(
            modifier = Modifier, navController = navController, uiState = uiState, vm = vm
        )
    }
}

@Composable
fun NavigateBluetoothDisabled(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate("bt_disabled")
    }
}

@Composable
internal fun EshareConnectingScreen(
    modifier: Modifier,
    navController: NavController,
    uiState: EshareConnectingUiState,
    vm: EshareConnectingViewModel
) {
    val TAG = "EshareConnectingScreen"
    Log.i(TAG, "EshareConnectingScreen: ${uiState.connectionState}")

    when (uiState.connectionState) {
        eShareConnectionStatus.Failed -> {
            Log.e(TAG, "eShare connection has failed. Show the error screen")
        }

        eShareConnectionStatus.Connected -> {
            Log.i(TAG, "EshareConnectingScreen: Should not hit here")
        }

        eShareConnectionStatus.Initiated -> {
            //start timer for timeout
            LaunchedEffect(Unit) {
                navController.navigate(EshareScreens.EshareConnectedRoute.route)
            }

        }

        eShareConnectionStatus.Unknown -> {
            //base state
            LaunchedEffect(Unit) {
                vm.startEshareConnection()
            }
            LoadingScreenWithSpinner(loadingText = "Connecting HCS",
                modifier = modifier,
                cancelButtonNeeded = true,
                onCancelButtonClicked = {
                    vm.onCancelClicked()
                    navController.popBackStack("home_first", false)
                })

        }


        eShareConnectionStatus.ReceivedUserRejection -> {
            //show rejection screen
        }

        eShareConnectionStatus.Timeout -> {
            //show timeout screen
        }

        eShareConnectionStatus.Disconnected -> {
            //show disconnected screen
        }

        else -> {}
    }

}