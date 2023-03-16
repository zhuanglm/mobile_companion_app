package com.esightcorp.mobile.app.btconnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.esightcorp.mobile.app.btconnection.*
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens.BtConnectedRoute.addrArg
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens.BtConnectedRoute.arguments
import com.esightcorp.mobile.app.btconnection.navigation.BtConnectionScreens.BtConnectedRoute.nameArg

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
        composable(BtConnectionScreens.BtConnectedRoute.routeWithArgs,
            arguments = arguments,
            deepLinks = listOf(navDeepLink {
                uriPattern = "android-app://androidx.navigation//${BtConnectionScreens.BtConnectedRoute.route}/{$addrArg}/{$nameArg}"
            })
        ){navBackStackEntry ->
            val deviceName = navBackStackEntry.arguments?.getString(nameArg)
            val deviceAddress = navBackStackEntry.arguments?.getString(addrArg)
            BtConnectedRoute(navController = navController)
        }


    }
}