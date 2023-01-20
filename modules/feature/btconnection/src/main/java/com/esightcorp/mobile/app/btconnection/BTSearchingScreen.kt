package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.LoadingScreenWithSpinner

@Composable
fun BtSearchingRoute(
    navController: NavController
) {
    BtSearchingScreen(
        modifier = Modifier,
        navController = navController
    )
}

@Composable
internal fun BtSearchingScreen(
    modifier: Modifier,
    navController: NavController
) {
    LoadingScreenWithSpinner(
        loadingText = "Searching for eSight devices",
        modifier = modifier
    )
}

