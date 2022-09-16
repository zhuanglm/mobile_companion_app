package com.esightcorp.mobile.app.companion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.HomeScreen

fun NavGraphBuilder.addGoNavigation(navController: NavController) {
    navigation(startDestination = GoScreens.HomeScreen.route, route=SupportedProducts.GoProduct.route){
        composable(GoScreens.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(GoScreens.EshareScreen.route){
            //Todo: Build eShare screen and reference it here
        }
        composable(GoScreens.BtConnectionScreen.route){
            //Todo: Build BT connection screen and reference it here
        }
        composable(GoScreens.WifiConnectionScreen.route){
            //Todo: Build wifi connection screen and reference it here
        }

    }
}