package com.esightcorp.mobile.app.eshare.composables

import android.util.Log
import android.view.TextureView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.eshare.AutoFitTextureView
import com.esightcorp.mobile.app.ui.components.eshare.RotateToLandscape
import com.esightcorp.mobile.app.ui.components.eshare.remote.ColorContrastButton
import com.esightcorp.mobile.app.ui.components.eshare.remote.EshareRemote
import com.esightcorp.mobile.app.utils.NavigateToBluetoothDisabled
import com.esightcorp.mobile.app.utils.NavigateToDeviceDisconnected
import com.esightcorp.mobile.app.utils.eShareConnectionStatus

@Composable
fun EshareConnectedRoute(
    navController: NavController,
    vm: EshareConnectedViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    if (!uiState.radioState.isBtEnabled) {
        Log.i(TAG, "EshareConnectedRoute: Bluetooth is disabled right now")
        NavigateToBluetoothDisabled(navController = navController)
    }

    if (!uiState.radioState.isWifiEnabled) {
        Log.i(TAG, "EshareConnectedRoute: Wifi is disabled right now")
    }

    if (!uiState.deviceConnectionState.isDeviceConnected) {
        Log.i(TAG, "EshareConnectedRoute: Device is not connected")
        NavigateToDeviceDisconnected(navController = navController)
    }
    if (uiState.radioState.isBtEnabled && uiState.deviceConnectionState.isDeviceConnected) {
        eShareConnectedScreen(
            textureViewListener = vm,
            uiState = uiState,
            navController = navController,
            startEshareConnection = vm::startEshareConnection,
            navigateToStoppedRoute = vm::navigateToStoppedRoute,
            navigateToUnableToConnectRoute = vm::navigateToUnableToConnectRoute,
            navigateToBusyRoute = vm::navigateToBusyRoute,
            onCancelButtonClicked = vm::onCancelButtonClicked,
            upButtonPress = vm::upButtonPress,
            downButtonPress = vm::downButtonPress,
            menuButtonPress = vm::menuButtonPress,
            modeButtonPress = vm::modeButtonPress,
            volUpButtonPress = vm::volUpButtonPress,
            volDownButtonPress = vm::volDownButtonPress,
            finderButtonPress = vm::finderButtonPress,
            actionUpButtonPress = vm::actionUpButtonPress,
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
    startEshareConnection: () -> Unit,
    navigateToStoppedRoute: (NavController) -> Unit,
    navigateToUnableToConnectRoute: (NavController) -> Unit,
    navigateToBusyRoute: (NavController) -> Unit,
    onCancelButtonClicked: () -> Unit,
    upButtonPress: () -> Unit = {},
    downButtonPress: () -> Unit = {},
    menuButtonPress: () -> Unit = {},
    modeButtonPress: () -> Unit = {},
    volUpButtonPress: () -> Unit = {},
    volDownButtonPress: () -> Unit = {},
    finderButtonPress: () -> Unit = {},
    actionUpButtonPress: () -> Unit = {},
) {
    Log.i(TAG, "eShareConnectedScreen: ")
    Row {
        TextureViewAndCancelButton(
            textureViewListener = textureViewListener,
            modifier = Modifier.weight(1f),
            onCancelButtonClicked = onCancelButtonClicked
        )
        EshareRemote(
            onFinderButtonPressedEventDown = finderButtonPress,
            onFinderButtonPressedEventUp = actionUpButtonPress,
            onModeButtonPressedEventDown = modeButtonPress,
            onModeButtonPressedEventUp = actionUpButtonPress,
            onUpButtonPressedEventDown = upButtonPress,
            onUpButtonPressedEventUp = actionUpButtonPress,
            onDownButtonPressedEventDown = downButtonPress,
            onDownButtonPressedEventUp = actionUpButtonPress,
            onVolumeUpButtonPressedEventDown = volUpButtonPress,
            onVolumeUpButtonPressedEventUp = actionUpButtonPress,
            onVolumeDownButtonPressedEventDown = volDownButtonPress,
            onVolumeDownButtonPressedEventUp = actionUpButtonPress,
            onMenuButtonPressedEventDown = menuButtonPress,
            onMenuButtonPressedEventUp = actionUpButtonPress,
        )
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
                    onCancelButtonClicked()
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

        eShareConnectionStatus.Failed -> {
            Log.e(TAG, "eShareConnectedScreen: FAILED")
        }

        eShareConnectionStatus.ReceivedUserRejection -> {
            Log.e(TAG, "eShareConnectedScreen: User rejection")
        }

        eShareConnectionStatus.Timeout -> {
            Log.e(TAG, "eShareConnectedScreen: TImoeut")
        }

        eShareConnectionStatus.Unknown -> {
            LaunchedEffect(Unit) {
                Log.i(TAG, "eShareConnectedScreen: Starting eShare Connection")
                startEshareConnection()
            }
        }
    }


}


@Composable
fun TextureViewAndCancelButton(
    textureViewListener: TextureView.SurfaceTextureListener,
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxHeight()) {
        AndroidView(factory = { context ->
            AutoFitTextureView(context).apply {
                surfaceTextureListener = textureViewListener
            }
        }, modifier = modifier
            .fillMaxHeight(), update = { view ->
            view.surfaceTextureListener = textureViewListener
            Log.i(TAG, "eShareConnectedScreen: TextureView updated ${view.isAvailable}")

        })
        StopEshareButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(40.dp, 40.dp),
            onCancelButtonClicked = {
                onCancelButtonClicked()
            })
    }

}

@Composable
fun StopEshareButton(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {}
) {
    ColorContrastButton(
        modifier = modifier,
        onClick = onCancelButtonClicked,
        primaryColor = Color.White,
        secondaryColor = Color.Red,
        icon = painterResource(id = R.drawable.close_eshare_button),
        size = 40.dp

    )

}





