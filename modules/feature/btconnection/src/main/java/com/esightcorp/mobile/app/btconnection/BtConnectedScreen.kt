package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon

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
    LoadingScreenWithIcon(modifier = modifier, loadingText = "Connected to ${deviceName}\nAddress: $deviceAddress")
}