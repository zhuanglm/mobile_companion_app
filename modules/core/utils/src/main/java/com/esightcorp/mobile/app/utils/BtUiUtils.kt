package com.esightcorp.mobile.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun NavigateToBluetoothDisabled(
    navController: NavController,
){
    LaunchedEffect(Unit){
        navController.navigate("bluetooth_disabled"){
            popUpTo("no_devices_found_bt"){
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}

@Composable
fun NavigateToDeviceDisconnected(
    navController: NavController
) {
    LaunchedEffect(Unit){
        navController.navigate("no_devices_found_bt"){
            popUpTo("no_devices_found_bt"){
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}