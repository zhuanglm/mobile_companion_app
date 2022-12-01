package com.esightcorp.mobile.app.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.HomeFirstScreen
import com.esightcorp.mobile.app.home.HomePermissionScreen

fun NavGraphBuilder.addHomeNavigation(navController: NavController) {
    navigation(startDestination = HomeScreens.HomePermissionScreen.route, route= HomeScreens.IncomingNavigationRoute.route){
        composable(HomeScreens.HomePermissionScreen.route){
            HomePermissionScreen(navController = navController)
        }
        composable(HomeScreens.HomeFirstScreen.route){
            HomeFirstScreen(navController = navController)
        }

    }
}