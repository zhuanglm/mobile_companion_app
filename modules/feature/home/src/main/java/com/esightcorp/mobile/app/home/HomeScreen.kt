package com.esightcorp.mobile.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController: NavController){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("wificonnection") }) {
            Text(text = "wifi")
        }
        Button(onClick = { navController.navigate("btconnection") }) {
            Text(text = "bluetooth")
        }
        Button(onClick = { navController.navigate("eshare") }) {
            Text(text = "eshare")
        }

    }
}