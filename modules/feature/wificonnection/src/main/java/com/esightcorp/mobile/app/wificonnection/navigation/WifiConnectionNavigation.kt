package com.esightcorp.mobile.app.wificonnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.navigation.WifiConnectionScreens
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreen

fun NavGraphBuilder.addWifiConnectionNavigation(navController: NavController) {
    navigation(startDestination = WifiConnectionScreens.WifiConnectionHomeScreen.route, route= WifiConnectionScreens.IncomingNavigationRoute.route){
        composable(WifiConnectionScreens.WifiConnectionHomeScreen.route){
            WifiConnectionScreen(navController = navController)
        }

    }
}