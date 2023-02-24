package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import kotlinx.coroutines.delay

@Composable
fun BtConnectedRoute(
    navController: NavController, deviceName: String?, deviceAddress: String?
) {
    BtConnectedScreen(
        navController = navController,
        modifier = Modifier,
        deviceAddress = deviceAddress,
        deviceName = deviceName
    )
}

@Composable
internal fun BtConnectedScreen(
    navController: NavController, modifier: Modifier, deviceAddress: String?, deviceName: String?
) {
    val homeRoute = stringResource(id = R.string.navigate_to_home) + "{$deviceName}"
    val screenTimeout = 5000L //5s in milliseconds
    if (deviceName != null && deviceAddress != null) {
        val loadingText = stringResource(id = R.string.connected_to) + "${
            deviceName.substring(
                1, deviceName.length - 1
            )
        }\n" + "Address: ${deviceAddress.substring(1, deviceAddress.length - 1)}"
        LoadingScreenWithIcon(modifier = modifier, loadingText = loadingText)
    }else{
        val loadingText = stringResource(id = R.string.something_went_wrong)
        LoadingScreenWithIcon(modifier = modifier, loadingText = loadingText)
    }
    LaunchedEffect(Unit) {
        delay(screenTimeout)
        navController.navigate(homeRoute)
    }
}