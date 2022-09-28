package com.esightcorp.mobile.app.wificonnection

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiConnectionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun WifiConnectionScreen(navController: NavController){
    Text(text = "wifi connection")
}