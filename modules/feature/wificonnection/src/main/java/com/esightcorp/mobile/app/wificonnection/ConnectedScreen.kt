package com.esightcorp.mobile.app.wificonnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import com.esightcorp.mobile.app.wificonnection.viewmodels.ConnectedViewModel
import kotlinx.coroutines.delay

@Composable
fun ConnectedRoute(
    navController: NavController,
    vm: ConnectedViewModel = hiltViewModel()
){
    ConnectedScreen(
        navController = navController,
        modifier = Modifier,

    )
}

@Composable
internal fun ConnectedScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    val homeRoute = "home_first"
    val screenTimeout = 5000L //5s in milliseconds
    LoadingScreenWithIcon(
        loadingText = "Wifi is now connected"
    )
    LaunchedEffect(Unit) {
        delay(screenTimeout)
        navController.navigate(homeRoute)
    }
}