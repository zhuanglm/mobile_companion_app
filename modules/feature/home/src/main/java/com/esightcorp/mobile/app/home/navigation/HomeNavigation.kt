/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.HomeFirstScreen
import com.esightcorp.mobile.app.home.LocationPermissionRoute
import com.esightcorp.mobile.app.home.LocationServiceOffRoute
import com.esightcorp.mobile.app.ui.extensions.composable
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.FirstScreenRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.LocationPermissionRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.LocationServiceOffRoute

fun NavGraphBuilder.addHomeNavigation(navController: NavController) {
    navigation(startDestination = FirstScreenRoute.path, route = IncomingRoute.path) {
        composable(FirstScreenRoute) { HomeFirstScreen(navController) }

        composable(LocationPermissionRoute) { LocationPermissionRoute(navController) }

        composable(LocationServiceOffRoute) { LocationServiceOffRoute(navController) }
    }
}
