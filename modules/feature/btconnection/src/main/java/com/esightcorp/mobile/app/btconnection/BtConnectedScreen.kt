package com.esightcorp.mobile.app.btconnection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.loading.LoadingScreenWithIcon

@Composable
fun BtConnectedRoute(
    navController: NavController
){
    BtConnectedScreen(navController = navController, modifier = Modifier)
}

@Composable
internal fun BtConnectedScreen(
    navController: NavController,
    modifier: Modifier
){
    LoadingScreenWithIcon(modifier = modifier, loadingText = "Connected to eSight")
}