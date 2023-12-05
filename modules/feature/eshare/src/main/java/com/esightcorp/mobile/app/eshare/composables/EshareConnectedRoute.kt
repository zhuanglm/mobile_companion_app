package com.esightcorp.mobile.app.eshare.composables

import android.graphics.SurfaceTexture
import android.util.Log
import android.view.TextureView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.navigation.EShareStoppedReason
import com.esightcorp.mobile.app.eshare.state.EshareConnectedUiState
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner
import com.esightcorp.mobile.app.ui.components.eshare.AutoFitTextureView
import com.esightcorp.mobile.app.ui.components.eshare.RotateToLandscape
import com.esightcorp.mobile.app.ui.components.eshare.remote.ColorContrastButton
import com.esightcorp.mobile.app.ui.components.eshare.remote.EshareRemote
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.utils.EShareConnectionStatus
import com.esightcorp.mobile.app.utils.NavigateToBluetoothDisabled
import com.esightcorp.mobile.app.utils.NavigateToDeviceDisconnected

@Composable
fun EshareConnectedRoute(
    navController: NavController,
    vm: EshareConnectedViewModel = hiltViewModel()
) {
//    BackStackLogger(navController, TAG)

    val uiState by vm.uiState.collectAsState()
    if (!uiState.radioState.isBtEnabled) {
        Log.i(TAG, "EshareConnectedRoute: Bluetooth is disabled right now")
        NavigateToBluetoothDisabled(navController = navController)
        return
    }

    if (!uiState.radioState.isWifiEnabled) {
        Log.i(TAG, "EshareConnectedRoute: Wifi is disabled right now")
        LaunchedEffect(Unit) { vm.navigateToWifiDisabledRoute(navController) }
        return
    }

    if (!uiState.deviceConnectionState.isDeviceConnected) {
        Log.i(TAG, "EshareConnectedRoute: Device is not connected")
        NavigateToDeviceDisconnected(navController = navController)
        return
    }

    if (uiState.radioState.isBtEnabled && uiState.deviceConnectionState.isDeviceConnected) {
        BackHandler { vm.onCancelButtonClicked(navController) }

        EShareConnectedScreen(
            textureViewListener = vm,
            uiState = uiState,
            navController = navController,
            startEshareConnection = vm::startEshareConnection,
            cancelEshareConnection = vm::cancelEshareConnection,
            navigateToStoppedRoute = vm::navigateToStoppedRoute,
            navigateToUnableToConnectRoute = vm::navigateToUnableToConnectRoute,
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
        return
    }
}

//region Internal implementation

private const val TAG = "EshareConnectedRoute"

@Composable
internal fun EShareConnectedScreen(
    textureViewListener: TextureView.SurfaceTextureListener,
    uiState: EshareConnectedUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
    startEshareConnection: OnActionCallback? = null,
    cancelEshareConnection: OnActionCallback? = null,
    navigateToStoppedRoute: ((NavController, EShareStoppedReason?) -> Unit)? = null,
    navigateToUnableToConnectRoute: OnNavigationCallback? = null,
    onCancelButtonClicked: OnNavigationCallback? = null,
    upButtonPress: OnActionCallback? = null,
    downButtonPress: OnActionCallback? = null,
    menuButtonPress: OnActionCallback? = null,
    modeButtonPress: OnActionCallback? = null,
    volUpButtonPress: OnActionCallback? = null,
    volDownButtonPress: OnActionCallback? = null,
    finderButtonPress: OnActionCallback? = null,
    actionUpButtonPress: OnActionCallback? = null,
) {
    Row {
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
        ) {
            TextureViewAndCancelButton(
                textureViewListener = textureViewListener,
                modifier = modifier,
                navController = navController,
                onClick = onCancelButtonClicked,
            )
        }

        EshareRemote(
            modifier = Modifier.weight(1f),
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

    Log.e(TAG, "eShare-connection state: ${uiState.connectionState}")
    when (uiState.connectionState) {
        // 1st state
        EShareConnectionStatus.Unknown -> {
            LaunchedEffect(Unit) {
                Log.i(TAG, "eShareConnectedScreen: Starting eShare Connection")
                startEshareConnection?.invoke()
            }
        }

        // Waiting for remote connection
        EShareConnectionStatus.Initiated -> {
            LoadingScreenWithSpinner(
                loadingText = stringResource(R.string.eshare_loading_text),
                modifier = modifier,
                cancelButtonNeeded = true,
                onCancelButtonClicked = { onCancelButtonClicked?.invoke(navController) },
            )
        }

        EShareConnectionStatus.Connected -> {
            RotateToLandscape()
        }

        EShareConnectionStatus.Disconnected -> {
            LaunchedEffect(Unit) {
                navigateToStoppedRoute?.invoke(
                    navController,
                    EShareStoppedReason.REMOTE_STOPPED
                )
            }
        }

        EShareConnectionStatus.Busy -> {
            LaunchedEffect(Unit) {
                navigateToStoppedRoute?.invoke(
                    navController,
                    EShareStoppedReason.EXISTING_ACTIVE_SESSION
                )
            }
        }

        EShareConnectionStatus.ReceivedUserRejection -> {
            LaunchedEffect(Unit) {
                navigateToStoppedRoute?.invoke(
                    navController,
                    EShareStoppedReason.USER_DECLINED
                )
            }
        }

        EShareConnectionStatus.Timeout,
        EShareConnectionStatus.IpNotReachable,
        EShareConnectionStatus.AddressNotAvailable -> {
            BackStackLogger(navController, TAG)

            LaunchedEffect(Unit) {
                // Send stop command to clean up current session
                cancelEshareConnection?.invoke()

                navigateToUnableToConnectRoute?.invoke(navController)
            }
        }
    }
}


@Composable
internal fun TextureViewAndCancelButton(
    textureViewListener: TextureView.SurfaceTextureListener,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: OnNavigationCallback? = null,
) {
    Box(modifier = Modifier.fillMaxHeight()) {
        AndroidView(
            factory = { context ->
                AutoFitTextureView(context).apply { surfaceTextureListener = textureViewListener }
            },
            modifier = modifier.align(Alignment.Center)
        )

        ColorContrastButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(20.dp, 20.dp),
            primaryColor = Color.White,
            secondaryColor = Color.Red,
            icon = R.drawable.close_eshare_button,
            size = 40.dp,
            onClick = { onClick?.invoke(navController) },
        )
    }
}

@Preview(name = "landscape", widthDp = 800, heightDp = 360)
@Composable
internal fun EShareConnectedScreenPreview() = MaterialTheme {
    EShareConnectedScreen(
        uiState = EshareConnectedUiState(connectionState = EShareConnectionStatus.Connected),
        navController = rememberNavController(),
        textureViewListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {}

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean = false

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
        }
    )
}
//endregion
