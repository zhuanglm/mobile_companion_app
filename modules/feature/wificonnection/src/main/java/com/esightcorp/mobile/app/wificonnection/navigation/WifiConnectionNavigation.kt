package com.esightcorp.mobile.app.wificonnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.esightcorp.mobile.app.wificonnection.*
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens.SearchingForNetworkRoute.arguments
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens.SearchingForNetworkRoute.flowArg

fun NavGraphBuilder.addWifiConnectionNavigation(navController: NavController) {
    navigation(startDestination = WifiConnectionScreens.WifiOffRoute.route, route= WifiConnectionScreens.IncomingNavigationRoute.route){
        /*composable(WifiConnectionScreens.WifiConnectionHomeScreen.route){
            WifiConnectionScreen(navController = navController)
        }*/
        composable(
            WifiConnectionScreens.EnterPasswordRoute.route
        ) {
            EnterPasswordRoute(navController = navController)
        }
        composable(
            WifiConnectionScreens.SearchingForNetworkRoute.routeWithArgs,
            arguments = arguments,
            deepLinks = listOf(navDeepLink {
                uriPattern =
                     "android-app://androidx.navigation//${WifiConnectionScreens.SearchingForNetworkRoute.route}/{$flowArg}"
            })
        ) { navBackStackEntry ->
            val flow = navBackStackEntry.arguments?.getString(flowArg)
            SearchingForNetworksRoute(navController = navController, flow = flow)
        }
        composable(WifiConnectionScreens.SelectNetworkRoute.route) {
            SelectNetworkRoute(navController = navController)
        }
        composable(WifiConnectionScreens.ConnectingRoute.route) {
            WifiConnectingRoute(navController = navController)
        }
        composable(WifiConnectionScreens.ConnectedRoute.route) {
            ConnectedRoute(navController = navController)
        }
        composable(WifiConnectionScreens.WifiOffRoute.route) {
            WifiOffRoute(navController = navController)
        }
        composable(WifiConnectionScreens.UnableToConnectRoute.route) {
            UnableToConnectRoute(navController = navController)
        }
        composable(WifiConnectionScreens.NoNetworksFoundRoute.route) {
            NoNetworksFoundRoute(navController = navController)
        }
        composable(WifiConnectionScreens.AlreadyConnectedRoute.route) {
            AlreadyConnectedRoute(navController = navController)
        }
        composable(WifiConnectionScreens.AdvancedNetworkSettingsRoute.route) {
            AdvancedWifiRoute(navController = navController)
        }
        composable(WifiConnectionScreens.SelectNetworkSecurityRoute.route){
            WifiTypeRoute(navController = navController)
        }
        composable(WifiConnectionScreens.WifiQRCodeRoute.route){
            WifiQRCodeRoute(navController = navController)
        }
    }
}