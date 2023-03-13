package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner

@Composable
fun BtConnectingRoute(
    navController: NavController
) {
    BtConnectingScreen(
        modifier = Modifier,
        navController = navController
    )
}

@Composable
internal fun BtConnectingScreen(
    modifier: Modifier,
    navController: NavController
) {
    LoadingScreenWithSpinner(
        loadingText = "Connecting to eSight",
        modifier = modifier
    )
}
