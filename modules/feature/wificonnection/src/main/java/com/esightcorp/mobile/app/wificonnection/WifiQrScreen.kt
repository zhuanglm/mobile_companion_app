/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToScanButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.rememberQrBitmapPainter
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader
import com.esightcorp.mobile.app.ui.extensions.BackStackLogger
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.wificonnection.state.WifiQrCodeUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiQrViewModel

@Composable
fun WifiQRCodeRoute(
    navController: NavController,
    vm: WifiQrViewModel = hiltViewModel(),
) {
    BackStackLogger(navController, TAG)

    val uiState by vm.uiState.collectAsState()
    WifiQrCodeScreen(
        navController = navController,
        uiState = uiState,
        onBackPressed = vm::onBackPressed,
        onHowToScanClicked = vm::onHowToScanClicked,
        onReturnToHomeClicked = vm::onGotoHomeScreen,
    )
}

//region Internal implementation

private const val TAG = "WifiQrScreen"

@Composable
private fun WifiQrCodeScreen(
    navController: NavController,
    uiState: WifiQrCodeUiState,
    modifier: Modifier = Modifier,
    onBackPressed: OnNavigationCallback? = null,
    onHowToScanClicked: OnNavigationCallback? = null,
    onReturnToHomeClicked: OnNavigationCallback? = null,
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    activity?.let { act ->
        SetScreenBrightnessToMax(window = act.window)

        DisposableEffect(Unit){
            onDispose {
                val layoutParams = act.window.attributes
                layoutParams.screenBrightness = -1f //reset value
                act.window.attributes = layoutParams
            }
        }
    }


    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        bottomButton = {
            HowToScanButton(
                modifier = modifier,
                onScanClick = { onHowToScanClicked?.invoke(navController) }
            )
        },
    ) {
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
    onReturnToHomeClicked: OnNavigationCallback? = null,
    navController: NavController,
    uiState: WifiQrCodeUiState
) {
    ConstraintLayout {
        val (header, subHeader, qrcode, button) = createRefs()

        Header1Text(
            text = stringResource(R.string.kWifiQRViewScanWiFiTitleText),
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
            },
        )

        BoldSubheader(
            text = stringResource(R.string.kQRViewScanWifiSubtitleText),
            modifier = modifier.constrainAs(subHeader) {
                top.linkTo(header.bottom, margin = 20.dp)
            },
        )

        Box(
            modifier = modifier
                .constrainAs(qrcode) {
                    top.linkTo(subHeader.bottom, margin = 20.dp)
                    bottom.linkTo(button.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(
                    color = MaterialTheme.colors.onSurface,
                    shape = RoundedCornerShape(25.dp),
                )
                .padding(25.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = rememberQrBitmapPainter(content = uiState.qrString),
                modifier = modifier,
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }

        TextRectangularButton(
            onClick = { onReturnToHomeClicked?.invoke(navController) },
            modifier = modifier.constrainAs(button) {
                bottom.linkTo(parent.bottom, margin = 20.dp)
            },
            text = stringResource(id = R.string.kQRViewReturnHomeButtonText)
        )
    }
}

@Composable
fun SetScreenBrightnessToMax(window: Window) {
    val layoutParams = window.attributes
    layoutParams.screenBrightness = 1f // 1f = 100%
    window.attributes = layoutParams
    
}

fun Context.findActivity(): Activity? = when(this){
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Preview
@Composable
private fun WifiQrCodeScreenPreview() = MaterialTheme {
    WifiQrCodeScreen(
        navController = rememberNavController(),
        uiState = WifiQrCodeUiState(qrString = "QR-Code-12345678"),
    )
}

//endregion
