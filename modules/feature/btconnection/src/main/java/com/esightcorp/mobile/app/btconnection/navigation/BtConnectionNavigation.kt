package com.esightcorp.mobile.app.btconnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.btconnection.BtConnectionScreen
import com.esightcorp.mobile.app.btconnection.BtDevicesScreen
import com.esightcorp.mobile.app.btconnection.BtDisabledScreen

fun NavGraphBuilder.addBtConnectionNavigation(navController: NavController) {
    navigation(startDestination = BtConnectionScreens.BtConnectionHomeScreen.route, route= BtConnectionScreens.IncomingNavigationRoute.route){
        composable(BtConnectionScreens.BtConnectionHomeScreen.route){
            BtConnectionScreen(navController = navController)
        }
        composable(BtConnectionScreens.BtDevicesScreen.route){
            BtDevicesScreen(navController = navController)
        }
        composable(BtConnectionScreens.BtDisabledScreen.route){
            BtDisabledScreen(navController = navController)
        }

    }
}