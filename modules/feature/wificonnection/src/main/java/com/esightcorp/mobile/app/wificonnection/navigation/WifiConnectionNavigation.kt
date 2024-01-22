package com.esightcorp.mobile.app.wificonnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.esightcorp.mobile.app.ui.extensions.composable
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation.ConnectedRoute
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation.ScanningRoute
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation.UnableToConnectRoute
import com.esightcorp.mobile.app.ui.navigation.WifiNavigation.WifiOffRoute
import com.esightcorp.mobile.app.wificonnection.AdvancedWifiRoute
import com.esightcorp.mobile.app.wificonnection.AlreadyConnectedRoute
import com.esightcorp.mobile.app.wificonnection.ConnectedRoute
import com.esightcorp.mobile.app.wificonnection.EnterPasswordRoute
import com.esightcorp.mobile.app.wificonnection.NoNetworksFoundRoute
import com.esightcorp.mobile.app.wificonnection.SearchingForNetworksRoute
import com.esightcorp.mobile.app.wificonnection.SelectNetworkRoute
import com.esightcorp.mobile.app.wificonnection.UnableToConnectRoute
import com.esightcorp.mobile.app.wificonnection.WifiConnectingRoute
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.WifiOffRoute
import com.esightcorp.mobile.app.wificonnection.WifiQRCodeRoute
import com.esightcorp.mobile.app.wificonnection.WifiTypeRoute

fun NavGraphBuilder.addWifiConnectionNavigation(navController: NavController) {
    navigation(startDestination = WifiOffRoute.path, route = IncomingRoute.path) {

        composable(WifiNavigation.AlreadyConnectedRoute) {
            AlreadyConnectedRoute(navController = navController)
        }

        composable(
            ScanningRoute.routeWithArgs,
            arguments = ScanningRoute.arguments,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "android-app://androidx.navigation//${ScanningRoute.routeWithArgs}"
                },
            )
        ) { navBackStackEntry ->
            val flow = ScanningRoute.getFlowArgument(navBackStackEntry.arguments)
            SearchingForNetworksRoute(navController = navController, flow = flow)
        }

        composable(ConnectedRoute) { ConnectedRoute(navController) }
        composable(UnableToConnectRoute) { UnableToConnectRoute(navController) }

        composable(
            WifiConnectionScreens.EnterPasswordRoute.route
        ) {
            EnterPasswordRoute(navController = navController)
        }

        composable(WifiConnectionScreens.SelectNetworkRoute.route) {
            SelectNetworkRoute(navController = navController)
        }
        composable(WifiConnectionScreens.ConnectingRoute.route) {
            WifiConnectingRoute(navController = navController)
        }

        composable(WifiConnectionScreens.WifiOffRoute.route) {
            WifiOffRoute(navController = navController)
        }

        composable(WifiConnectionScreens.NoNetworksFoundRoute.route) {
            NoNetworksFoundRoute(navController = navController)
        }
        composable(WifiConnectionScreens.AdvancedNetworkSettingsRoute.route) {
            AdvancedWifiRoute(navController = navController)
        }
        composable(WifiConnectionScreens.SelectNetworkSecurityRoute.route) {
            WifiTypeRoute(navController = navController)
        }
        composable(WifiConnectionScreens.WifiQRCodeRoute.route) {
            WifiQRCodeRoute(navController = navController)
        }
    }
}