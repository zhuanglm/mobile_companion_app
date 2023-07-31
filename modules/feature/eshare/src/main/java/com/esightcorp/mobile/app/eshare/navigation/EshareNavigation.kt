package com.esightcorp.mobile.app.eshare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.eshare.EshareConnectingRoute


fun NavGraphBuilder.addEshareNavigation(navController: NavController) {
    navigation(startDestination = EshareScreens.EshareConnectingRoute.route, route= EshareScreens.IncomingNavigationRoute.route){
        composable(EshareScreens.EshareConnectingRoute.route){
            EshareConnectingRoute(navController = navController)
        }

    }
}