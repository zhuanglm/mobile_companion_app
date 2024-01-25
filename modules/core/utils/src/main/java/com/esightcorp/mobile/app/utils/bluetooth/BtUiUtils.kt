/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.utils.bluetooth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun NavigateToBluetoothDisabled(
    navController: NavController,
) {
    //TODO: refine this implementation again!!!
    LaunchedEffect(Unit) {
        navController.navigate("bluetooth_disabled") {
            popUpTo("no_devices_found_bt") {
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
    //TODO: refine this implementation again!!!
    LaunchedEffect(Unit) {
        navController.navigate("no_devices_found_bt") {
            popUpTo("no_devices_found_bt") {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}