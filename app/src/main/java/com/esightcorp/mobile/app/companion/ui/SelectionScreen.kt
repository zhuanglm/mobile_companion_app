/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.companion.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.esightcorp.mobile.app.companion.navigation.toplevel.SupportedProducts

private const val TAG = "SelectionScreen"

@Composable
fun SelectionScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.i(TAG, "SelectionScreen - Navigating to product screen ...")

        navController.navigate(SupportedProducts.GoProduct.route) {
            launchSingleTop = true
        }
    }
}
