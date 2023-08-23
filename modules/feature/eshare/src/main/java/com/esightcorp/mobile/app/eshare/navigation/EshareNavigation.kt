package com.esightcorp.mobile.app.eshare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.eshare.EshareConnectedRoute
import com.esightcorp.mobile.app.eshare.EshareConnectingRoute
import com.esightcorp.mobile.app.eshare.EshareConnectionStoppedRoute
import com.esightcorp.mobile.app.eshare.EshareUnableToConnectRoute
import com.esightcorp.mobile.app.eshare.HotspotSetupRoute


fun NavGraphBuilder.addEshareNavigation(navController: NavController) {
    navigation(startDestination = EshareScreens.EshareConnectingRoute.route, route= EshareScreens.IncomingNavigationRoute.route){
        composable(EshareScreens.EshareConnectingRoute.route){
            EshareConnectingRoute(navController = navController)
        }
        composable(EshareScreens.EshareConnectedRoute.route){
            EshareConnectedRoute(navController = navController)
        }
        composable(EshareScreens.EshareConnectionStoppedRoute.route){
            EshareConnectionStoppedRoute(navController = navController)
        }
        composable(EshareScreens.EshareUnableToConnectRoute.route){
            EshareUnableToConnectRoute(navController = navController)
        }
        composable(EshareScreens.HotspotSetupRoute.route){
            HotspotSetupRoute(navController = navController)
        }

    }
}