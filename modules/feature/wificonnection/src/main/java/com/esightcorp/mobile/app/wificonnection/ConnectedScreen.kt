package com.esightcorp.mobile.app.wificonnection

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import com.esightcorp.mobile.app.ui.components.viewmodel.ESightBaseViewModel
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import kotlinx.coroutines.delay

@Composable
fun ConnectedRoute(
    navController: NavController,
    vm: ESightBaseViewModel = hiltViewModel()
) {
    ConnectedScreen(
        navController = navController,
        modifier = Modifier,
        onGotoHomeScreen = { nav ->
            vm.gotoMainScreen(nav, popUntil = WifiNavigation.IncomingRoute)
        },
    )
}

//region Internal implementation

@Composable
private fun ConnectedScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onGotoHomeScreen: OnNavigationCallback? = null,
) {
    LoadingScreenWithIcon(
        modifier = modifier,
        loadingText = stringResource(R.string.wifi_connected_text),
    )

    LaunchedEffect(Unit) {
        delay(SCREEN_TIMEOUT)
        onGotoHomeScreen?.invoke(navController)
    }
}

@Preview
@Composable
private fun ConnectedScreenPreview() = MaterialTheme {
    ConnectedScreen(navController = rememberNavController())
}

private const val SCREEN_TIMEOUT = 5000L //5s in milliseconds
//endregion
