package com.esightcorp.mobile.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.eshare.navigation.EshareScreens
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens


@Composable
fun HomeFirstScreen(
    navController: NavController){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(WifiConnectionScreens.IncomingNavigationRoute.route) }) {
            Text(text = "wifi")
        }
        Button(onClick = { navController.navigate(BtConnectionScreens.IncomingNavigationRoute.route) }) {
            Text(text = "bluetooth")
        }
        Button(onClick = { navController.navigate(EshareScreens.IncomingNavigationRoute.route) }) {
            Text(text = "eshare")
        }

    }
}