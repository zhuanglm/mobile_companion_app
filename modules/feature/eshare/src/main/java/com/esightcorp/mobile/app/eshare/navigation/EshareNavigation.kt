package com.esightcorp.mobile.app.eshare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.eshare.EshareScreen

fun NavGraphBuilder.addEshareNavigation(navController: NavController) {
    navigation(startDestination = EshareScreens.EshareHomeScreen.route, route= EshareScreens.IncomingNavigationRoute.route){
        composable(EshareScreens.EshareHomeScreen.route){
            EshareScreen(navController = navController)
        }

    }
}