/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

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
import com.esightcorp.mobile.app.wificonnection.HowToScanRoute
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
            ),
        ) { navBackStackEntry ->
            val flow = ScanningRoute.getFlowArgument(navBackStackEntry.arguments)
            SearchingForNetworksRoute(navController = navController, flow = flow)
        }
        composable(WifiNavigation.SelectNetworkRoute) { SelectNetworkRoute(navController) }
        composable(WifiNavigation.NoNetworksFoundRoute) { NoNetworksFoundRoute(navController) }

        composable(ConnectedRoute) { ConnectedRoute(navController) }
        composable(WifiNavigation.HowToScanRoute) { HowToScanRoute(navController) }
        composable(UnableToConnectRoute) { UnableToConnectRoute(navController) }
        composable(WifiOffRoute) { WifiOffRoute(navController) }

        composable(WifiNavigation.EnterPasswordRoute) { EnterPasswordRoute(navController) }


        composable(WifiConnectionScreens.ConnectingRoute.route) {
            WifiConnectingRoute(navController = navController)
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