package com.esightcorp.mobile.app.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.HomeFirstScreen
import com.esightcorp.mobile.app.home.HomePermissionScreen
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.FirstScreenRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.HomeNavigation.PermissionRoute
import com.esightcorp.mobile.app.ui.extensions.composable

fun NavGraphBuilder.addHomeNavigation(navController: NavController) {
    navigation(startDestination = PermissionRoute.path, route = IncomingRoute.path) {
        composable(PermissionRoute) { HomePermissionScreen(navController) }

        composable(FirstScreenRoute) { HomeFirstScreen(navController) }
    }
}