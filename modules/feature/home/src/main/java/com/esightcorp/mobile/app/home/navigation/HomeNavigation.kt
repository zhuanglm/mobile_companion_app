package com.esightcorp.mobile.app.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.home.HomeFirstScreen

fun NavGraphBuilder.addHomeNavigation(navController: NavController) {
    navigation(startDestination = HomeScreens.HomeFirstScreen.route, route= HomeScreens.IncomingNavigationRoute.route){
        composable(HomeScreens.HomeFirstScreen.route){
            HomeFirstScreen(navController = navController)
        }

    }
}