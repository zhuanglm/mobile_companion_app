package com.esightcorp.mobile.app.wificonnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.wificonnection.*
import com.esightcorp.mobile.app.wificonnection.EnterPasswordRoute

fun NavGraphBuilder.addWifiConnectionNavigation(navController: NavController) {
    navigation(startDestination = WifiConnectionScreens.SearchingForNetworkRoute.route, route= WifiConnectionScreens.IncomingNavigationRoute.route){
        /*composable(WifiConnectionScreens.WifiConnectionHomeScreen.route){
            WifiConnectionScreen(navController = navController)
        }*/
        composable(
            WifiConnectionScreens.EnterPasswordRoute.route){
            EnterPasswordRoute(navController = navController)
        }
        composable(WifiConnectionScreens.SearchingForNetworkRoute.route){
            SearchingForNetworksRoute(navController = navController)
        }
        composable(WifiConnectionScreens.SelectNetworkRoute.route){
            SelectNetworkRoute(navController = navController)
        }
        composable(WifiConnectionScreens.ConnectingRoute.route){
            WifiConnectingRoute(navController = navController)
        }
        composable(WifiConnectionScreens.ConnectedRoute.route){
            ConnectedRoute(navController = navController)
        }
        composable(WifiConnectionScreens.WifiOffRoute.route){
            WifiOffRoute(navController = navController)
        }
        composable(WifiConnectionScreens.UnableToConnectRoute.route){
            UnableToConnectRoute(navController = navController)
        }
        composable(WifiConnectionScreens.NoNetworksFoundRoute.route){
            NoNetworksFoundRoute(navController = navController)
        }
        composable(WifiConnectionScreens.AlreadyConnectedRoute.route){
            AlreadyConnectedRoute(navController = navController)
        }
    }
}