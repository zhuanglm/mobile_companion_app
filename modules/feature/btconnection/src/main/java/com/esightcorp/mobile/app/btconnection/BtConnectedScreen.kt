package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon
import kotlinx.coroutines.delay

@Composable
fun BtConnectedRoute(
    navController: NavController,
    deviceName: String?,
    deviceAddress: String?
){
    BtConnectedScreen(navController = navController, modifier = Modifier, deviceAddress = deviceAddress, deviceName = deviceName)
}

@Composable
internal fun BtConnectedScreen(
    navController: NavController,
    modifier: Modifier,
    deviceAddress: String?,
    deviceName: String?
){
    if (deviceName != null && deviceAddress != null) {
        LoadingScreenWithIcon(modifier = modifier, loadingText = "Connected to ${deviceName.substring(1, deviceName.length -1)}\nAddress: ${deviceAddress.substring(1, deviceAddress.length -1)}")
    }
    LaunchedEffect(Unit){
        delay(5000)
        navController.navigate("home_first/{$deviceName}")
    }
}