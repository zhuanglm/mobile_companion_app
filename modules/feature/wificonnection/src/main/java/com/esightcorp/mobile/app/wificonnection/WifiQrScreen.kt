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
import com.esightcorp.mobile.app.ui.components.*
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.HowToScanButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.wificonnection.state.WifiQrCodeUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiQrViewModel

@Composable
fun WifiQRCodeRoute(
    navController: NavController, vm: WifiQrViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    WifiQrCodeScreen(
        modifier = Modifier,
        onBackPressed = vm::onBackPressed,
        onHowToScanClicked = vm::onHowToScanClicked,
        navController = navController,
        uiState = uiState
    )


}

@Composable
internal fun WifiQrCodeScreen(
    modifier: Modifier,
    onBackPressed: (NavController) -> Unit,
    onHowToScanClicked: (NavController) -> Unit,
    navController: NavController,
    uiState: WifiQrCodeUiState

) {
    BaseScreen(modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed(navController) },
        onSettingsButtonInvoked = { /*Unused*/ },
        bottomButton = {
            HowToScanButton(modifier = modifier,
                onScanClick = { onHowToScanClicked(navController) })
        }) {
        WifiQrCodeScreenBody(
            modifier = modifier
        )
    }
}

@Composable
private fun WifiQrCodeScreenBody(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, subheader, qrcode, button) = createRefs()
        Header1Text(text = stringResource(id = R.string.wifi_qr_code_header),
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
            })
        Subheader(text = stringResource(id = R.string.wifi_qr_code_subheader),
            modifier = modifier.constrainAs(subheader) {
                top.linkTo(header.bottom, margin = 10.dp)
                start.linkTo(parent.start)
            })

        Box(modifier =modifier.constrainAs(qrcode) {
            top.linkTo(subheader.bottom, margin = 35.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.background(color = MaterialTheme.colors.onSurface, shape = RoundedCornerShape(25.dp) ).padding(25.dp).fillMaxSize(), contentAlignment = Alignment.Center){
            Image(painter = rememberQrBitmapPainter(content = "testQr"), modifier = modifier, contentScale = ContentScale.FillBounds, contentDescription = null)
        }



        TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(button) {
            top.linkTo(qrcode.bottom, margin = 35.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = stringResource(id = R.string.wifi_qr_code_button))


    }
}