/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.companion.navigation.toplevel

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.companion.navigation.go.addGoNavigation
import com.esightcorp.mobile.app.companion.ui.SelectionScreen

@Composable
fun TopLevelNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SupportedProducts.SelectionScreen.route
    ) {
        composable(SupportedProducts.SelectionScreen.route) {
            SelectionScreen(navController = navController)
        }

        //add all nested nav graphs below
        addGoNavigation(navController)
    }
}
