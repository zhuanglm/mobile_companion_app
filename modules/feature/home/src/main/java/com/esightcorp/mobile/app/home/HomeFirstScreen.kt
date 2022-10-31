package com.esightcorp.mobile.app.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens
import com.esightcorp.mobile.app.eshare.navigation.EshareScreens
import com.esightcorp.mobile.app.home.viewmodels.HomeViewModel
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens


@Composable
fun HomeFirstScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel()){
    Log.d("TAG", "HomeFirstScreen: ")
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(.80f),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier.wrapContentHeight()) {
                Text(text = "Good evening ## TIME", style = MaterialTheme.typography.h2)
                Text(
                    text = "You are not connected to an eSight #### BLUETOOTH PERMISSIONS",
                    style = MaterialTheme.typography.caption
                )
            }
        }
        Button(onClick = { navController.navigate(BtConnectionScreens.IncomingNavigationRoute.route) },
            Modifier
                .padding(20.dp, 5.dp)
                .fillMaxWidth(0.75f)
        ) {
            Text(text = "Connect to a bluetooth device")
        }


        Button(onClick = { navController.navigate(WifiConnectionScreens.IncomingNavigationRoute.route) }) {
            Text(text = "wifi")
        }

        Button(onClick = { navController.navigate(EshareScreens.IncomingNavigationRoute.route) }) {
            Text(text = "eshare")
        }

    }
}

