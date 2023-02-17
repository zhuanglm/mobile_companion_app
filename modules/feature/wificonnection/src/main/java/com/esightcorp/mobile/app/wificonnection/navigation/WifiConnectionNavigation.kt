package com.esightcorp.mobile.app.wificonnection.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.esightcorp.mobile.app.wificonnection.*
import com.esightcorp.mobile.app.wificonnection.WifiCredentialsRoute

fun NavGraphBuilder.addWifiConnectionNavigation(navController: NavController) {
    navigation(startDestination = WifiConnectionScreens.WifiConnectionHomeScreen.route, route= WifiConnectionScreens.IncomingNavigationRoute.route){
        composable(WifiConnectionScreens.WifiConnectionHomeScreen.route){
            WifiConnectionScreen(navController = navController)
        }
        composable(
            WifiConnectionScreens.WifiCredentialsScreen.route/*,
            arguments = arguments,
            deepLinks = listOf( navDeepLink {
                uriPattern = "android-app://androidx.navigation//${WifiConnectionScreens.WifiCredentialsScreen.route}/{$ssidArg}"
            })*/
        ){ /*navBackStackEntry ->
            val ssid = navBackStackEntry.arguments?.getString(ssidArg)
            Log.d("TAG", "addWifiConnectionNavigation: $ssid")
            ssid?.let {*/ WifiCredentialsRoute(navController = navController)/*, ssid = it*/ /*}*/
        }
        composable(WifiConnectionScreens.SearchingForNetworkRoute.route){
            SearchingForNetworksRoute(navController = navController)
        }
        composable(WifiConnectionScreens.SelectNetworkRoute.route){
            SelectNetworkRoute(navController = navController)
        }
    }
}