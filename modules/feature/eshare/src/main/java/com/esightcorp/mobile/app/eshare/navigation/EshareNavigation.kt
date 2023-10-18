package com.esightcorp.mobile.app.eshare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.eshare.composables.EshareBusyRoute
import com.esightcorp.mobile.app.eshare.composables.EshareConnectedRoute
import com.esightcorp.mobile.app.eshare.composables.EshareConnectionStoppedRoute
import com.esightcorp.mobile.app.eshare.composables.EshareUnableToConnectRoute
import com.esightcorp.mobile.app.eshare.composables.HotspotSetupRoute


fun NavGraphBuilder.addEshareNavigation(navController: NavController) {
    navigation(startDestination = EshareScreens.EshareConnectedRoute.route, route= EshareScreens.IncomingNavigationRoute.route){
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
        composable(EshareScreens.EshareBusyRoute.route){
            EshareBusyRoute(navController = navController)
        }

    }
}