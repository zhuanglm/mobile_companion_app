/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.HomeFirstScreen
import com.esightcorp.mobile.app.home.HomePermissionScreen
import com.esightcorp.mobile.app.ui.extensions.composable
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.FirstScreenRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.PermissionRoute

fun NavGraphBuilder.addHomeNavigation(navController: NavController) {
    navigation(startDestination = PermissionRoute.path, route = IncomingRoute.path) {
        composable(PermissionRoute) { HomePermissionScreen(navController) }

        composable(FirstScreenRoute) { HomeFirstScreen(navController) }
    }
}