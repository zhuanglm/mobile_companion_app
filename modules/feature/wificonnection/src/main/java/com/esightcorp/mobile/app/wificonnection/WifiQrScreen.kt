package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToScanButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.rememberQrBitmapPainter
import com.esightcorp.mobile.app.wificonnection.state.WifiQrCodeUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiQrViewModel

@Composable
fun WifiQRCodeRoute(
    navController: NavController, vm: WifiQrViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
//    SetMaxScreenBrightness()
    WifiQrCodeScreen(
        modifier = Modifier,
        onBackPressed = vm::onBackPressed,
        onHowToScanClicked = vm::onHowToScanClicked,
        onReturnToHomeClicked = vm::onReturnToHomeClicked,
        navController = navController,
        uiState = uiState
    )


}

@Composable
internal fun WifiQrCodeScreen(
    modifier: Modifier,
    onBackPressed: (NavController) -> Unit,
    onHowToScanClicked: (NavController) -> Unit,
    onReturnToHomeClicked: (NavController) -> Unit,
    navController: NavController,
    uiState: WifiQrCodeUiState
) {
    BaseScreen(modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed(navController) },
        onSettingsButtonInvoked = { /*Unused*/ },
        bottomButton = {
            HowToScanButton(
                modifier = modifier,
                onScanClick = { onHowToScanClicked(navController) })
        }) {
        WifiQrCodeScreenBody(
            modifier = modifier,
            onReturnToHomeClicked = onReturnToHomeClicked,
            navController = navController,
            uiState = uiState
        )
    }
}

@Composable
private fun WifiQrCodeScreenBody(
    modifier: Modifier = Modifier,
    onReturnToHomeClicked: (NavController) -> Unit,
    navController: NavController,
    uiState: WifiQrCodeUiState
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subheader, qrcode, button) = createRefs()
        Header1Text(text = stringResource(id = R.string.kWifiQRViewScanWiFiTitleText),
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
            })
        Subheader(text = stringResource(id = R.string.kQRViewScanWifiSubtitleText),
            modifier = modifier.constrainAs(subheader) {
                top.linkTo(header.bottom, margin = 10.dp)
                start.linkTo(parent.start)
            })

        Box(modifier = modifier
            .constrainAs(qrcode) {
                top.linkTo(subheader.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .background(
                color = MaterialTheme.colors.onSurface, shape = RoundedCornerShape(25.dp)
            )
            .padding(25.dp)
            .fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = rememberQrBitmapPainter(content = uiState.qrString),
                modifier = modifier,
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }

        TextRectangularButton(onClick = { onReturnToHomeClicked(navController) },
            modifier = modifier.constrainAs(button) {
                top.linkTo(qrcode.bottom, margin = 35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(id = R.string.kQRViewReturnHomeButtonText)
        )


    }
}
// Can't use this, since it requires a protected permission that is only granted to system apps
//Android.permission.WRITE_SETTINGS
//@Composable
//fun SetMaxScreenBrightness() {
//    val context = LocalContext.current
//
//    // State variable to store the previous brightness value
//    var previousBrightness by remember { mutableStateOf(0) }
//
//    // Get the current screen brightness mode and value
//    val mode = Settings.System.getInt(
//        context.contentResolver,
//        Settings.System.SCREEN_BRIGHTNESS_MODE
//    )
//    previousBrightness = Settings.System.getInt(
//        context.contentResolver,
//        Settings.System.SCREEN_BRIGHTNESS
//    )
//
//    // If the screen brightness mode is set to automatic, turn it off first
//    if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
//        Settings.System.putInt(
//            context.contentResolver,
//            Settings.System.SCREEN_BRIGHTNESS_MODE,
//            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
//        )
//    }
//
//    // Set the screen brightness to the maximum value
//    val maxBrightness = 255
//    Settings.System.putInt(
//        context.contentResolver,
//        Settings.System.SCREEN_BRIGHTNESS,
//        maxBrightness
//    )
//
//    // Observe the lifecycle events to reset the brightness value
//    DisposableEffect(Unit) {
//        val coroutineScope = CoroutineScope(coroutineContext)
//        onDispose {
//            coroutineScope.launch {
//                Settings.System.putInt(
//                    context.contentResolver,
//                    Settings.System.SCREEN_BRIGHTNESS,
//                    previousBrightness
//                )
//            }
//        }
//    }
//}
