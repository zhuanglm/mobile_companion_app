package com.esightcorp.mobile.app.companion.navigation.go

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.btconnection.BtConnectionScreen
import com.esightcorp.mobile.app.companion.navigation.toplevel.SupportedProducts
import com.esightcorp.mobile.app.eshare.EshareScreen
import com.esightcorp.mobile.app.home.HomeScreen
import com.esightcorp.mobile.app.wificonnection.WifiConnectionScreen

fun NavGraphBuilder.addGoNavigation(navController: NavController) {
    navigation(startDestination = GoScreens.HomeScreen.route, route= SupportedProducts.GoProduct.route){
        composable(GoScreens.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(GoScreens.EshareScreen.route){
            EshareScreen(navController = navController)
        }
        composable(GoScreens.BtConnectionScreen.route){
            BtConnectionScreen(navController = navController)
        }
        composable(GoScreens.WifiConnectionScreen.route){
            WifiConnectionScreen(navController = navController)
        }

    }
}