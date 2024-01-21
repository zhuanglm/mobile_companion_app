package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.wificonnection.state.WifiOffUiState
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiOffViewModel

@Composable
fun WifiOffRoute(
    navController: NavController,
    vm: WifiOffViewModel = hiltViewModel()
) {
    vm.setNavController(navController)

    val isConnected by vm.connectionStateFlow().collectAsState()
    if (isConnected == false) {
        LaunchedEffect(Unit) { vm.onBleDisconnected(navController) }
        return
    }

    val uiState by vm.uiState.collectAsState()
    WifiOffScreen(
        navController = navController,
        onBackPressed = vm::onBackClicked,
        onWifiTurnedOn = vm::navigateHome,
        uiState = uiState,
    )

}

@Composable
internal fun WifiOffScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onWifiTurnedOn: () -> Unit,
    uiState: WifiOffUiState
) {
    val TAG = "WifiOffScreen"
    val headerTopMargin = dimensionResource(id = R.dimen.bt_disabled_header_top_margin)
    val bodyTopMargin = dimensionResource(id = R.dimen.bt_disabled_body_top_margin)

    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = onBackPressed,
        onSettingsButtonInvoked = { Unit },
        isBottomButtonNeeded = false,
        bottomButton = { Unit }) {
        WifiOffScreenBody(
            modifier = Modifier.fillMaxSize(),
            headerTopMargin = headerTopMargin,
            bodyTopMargin = bodyTopMargin
        )
    }
    if (uiState.isWifiEnabled) {
        LaunchedEffect(Unit) {
            onWifiTurnedOn()
        }
    }

}

@Composable
private fun WifiOffScreenBody(
    modifier: Modifier,
    headerTopMargin: Dp,
    bodyTopMargin: Dp
) {
    ConstraintLayout {
        val (bigIcon, headerText, header2Text, retry, cancel) = createRefs()

        BigIcon(
            painter = painterResource(id = R.drawable.round_wifi_24),
            contentDescription = stringResource(R.string.content_desc_wifi_icon),
            modifier = modifier.constrainAs(bigIcon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })

        Header1Text(
            text = stringResource(id = R.string.kWifiErrorWifiDisabledTitle),
            modifier = modifier.constrainAs(headerText) {
                top.linkTo(bigIcon.bottom, margin = headerTopMargin)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        Subheader(
            text = stringResource(id = R.string.kWifiErrorWifiDisabledSubtitle),
            modifier = modifier
                .padding(
                    dimensionResource(id = R.dimen.bt_disabled_horizontal_padding),
                    dimensionResource(
                        id = R.dimen.zero
                    )
                )
                .constrainAs(header2Text) {
                    top.linkTo(headerText.bottom, margin = bodyTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            textAlign = TextAlign.Center

        )
        TextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(retry){

        }, text = stringResource(
            id = R.string.kRetryButtonTitle
        ))
        OutlinedTextRectangularButton(onClick = { /*TODO*/ }, modifier = modifier.constrainAs(cancel){

        }, text = stringResource(id = R.string.kCancel))

        /* This code block will actually launch settings for us
            TODO: check if we want this with product

        val launcher = rememberLauncherForActivityResult(
             contract = ActivityResultContracts.StartActivityForResult(),
             onResult = {
                 Log.d("TAG", "isWifiEnabled: $it")
             }
         )

         DisposableEffect(Unit) {
             val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
             launcher.launch(intent)
             onDispose {
                 // clean up any resources if needed
             }
         }*/

    }
}

@Composable
fun NavigateToWifiOffScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate(WifiConnectionScreens.WifiOffRoute.route)

    }
}