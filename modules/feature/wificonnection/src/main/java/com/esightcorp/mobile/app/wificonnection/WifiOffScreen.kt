package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ItemSpacer
import com.esightcorp.mobile.app.ui.components.buttons.OutlinedTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiOffViewModel

@Composable
fun WifiOffRoute(
    navController: NavController,
    vm: WifiOffViewModel = hiltViewModel()
) {
    vm.setNavController(navController)
    val uiState by vm.uiState.collectAsState()
    WifiOffScreen(
        navController = navController,
        onBackPressed = vm::gotoMainScreen,
        onRetryPressed = vm::onRetryPressed,
        onWifiTurnedOn = vm::navigateHome,
        isWifiEnabled = uiState.isWifiEnabled,
        onCancelPressed = vm::gotoMainScreen,
        //onTryAgain = {}
    )

}

@Preview
@Composable
private fun WifiOffScreenPreview() = MaterialTheme {
    WifiOffScreen(navController = rememberNavController(),
        onWifiTurnedOn = {})
}

@Composable
internal fun WifiOffScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onRetryPressed: OnNavigationCallback? = null,
    onCancelPressed: OnNavigationCallback? = null,
    onBackPressed: OnNavigationCallback? = null,
    onWifiTurnedOn: () -> Unit,
    isWifiEnabled: Boolean = false,
    //onTryAgain: () -> Unit
) {
    //val TAG = "WifiOffScreen"
    val headerTopMargin = dimensionResource(id = R.dimen.bt_disabled_header_top_margin)
    val bodyTopMargin = dimensionResource(id = R.dimen.bt_disabled_body_top_margin)

    BaseScreen(
        modifier = modifier,
        showBackButton = true,
        showSettingsButton = false,
        onBackButtonInvoked = { onBackPressed?.invoke(navController) },
        onSettingsButtonInvoked = { },
        isBottomButtonNeeded = false,
        bottomButton = { },
    ) {
        Column(
            modifier = modifier.padding(vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BigIcon(drawableId = R.drawable.round_wifi_24)

            ItemSpacer(headerTopMargin)
            Header1Text(
                text = stringResource(R.string.kWifiErrorWifiDisabledTitle),
                modifier = modifier,
                textAlign = TextAlign.Center,
            )

            ItemSpacer(bodyTopMargin)
            Subheader(
                text = stringResource(R.string.kWifiErrorWifiDisabledSubtitle),
                modifier = modifier,
                textAlign = TextAlign.Center
            )

            ItemSpacer(50.dp)
            TextRectangularButton(
                onClick = { onRetryPressed?.invoke(navController) },
                modifier = modifier,
                text = stringResource(R.string.kRetryButtonTitle),
                textAlign = TextAlign.Center,
            )

            ItemSpacer(20.dp)
            OutlinedTextRectangularButton(
                onClick = { onCancelPressed?.invoke(navController)},
                modifier = modifier,
                text = stringResource(id = R.string.kCancel),
                textAlign = TextAlign.Center,
                textColor = MaterialTheme.colors.onSurface,
            )
        }
    }

    if (isWifiEnabled) {
        LaunchedEffect(Unit) {
            onWifiTurnedOn()
        }
    }

}

@Composable
fun NavigateToWifiOffScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        navController.navigate(WifiConnectionScreens.WifiOffRoute.route)

    }
}