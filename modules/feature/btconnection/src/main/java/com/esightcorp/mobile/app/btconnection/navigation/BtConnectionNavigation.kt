package com.esightcorp.mobile.app.btconnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.btconnection.*


fun NavGraphBuilder.addBtConnectionNavigation(navController: NavController) {
    navigation(startDestination = BtConnectionScreens.NoDevicesConnectedRoute.route, route= BtConnectionScreens.IncomingNavigationRoute.route){
        composable(BtConnectionScreens.NoDevicesConnectedRoute.route){
            NoDeviceConnectedRoute(navController = navController)
        }
        composable(BtConnectionScreens.BtDevicesScreen.route){
            BtDevicesRoute(navController = navController)
        }
        composable(BtConnectionScreens.BtDisabledScreen.route){
            BtDisabledScreen(navController = navController)
        }
        composable(BtConnectionScreens.BtSearchingRoute.route){
            BtSearchingRoute(navController = navController)
        }
        composable(BtConnectionScreens.BTConnectingRoute.route){
            BtConnectingRoute(navController = navController)
        }
        composable(BtConnectionScreens.UnableToConnectRoute.route){
            UnableToConnectRoute(navController = navController)
        }
        composable(BtConnectionScreens.NoDevicesFoundRoute.route){
            NoDevicesFoundRoute(navController = navController)
        }
        composable(BtConnectionScreens.BtConnectedRoute.route){
            BtConnectedRoute(navController = navController)
        }


    }
}