package com.esightcorp.mobile.app.home.navigation

import android.util.Log
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.esightcorp.mobile.app.home.HomeFirstScreen
import com.esightcorp.mobile.app.home.HomePermissionScreen
import com.esightcorp.mobile.app.home.navigation.HomeScreens.HomeFirstScreen.arguments
import com.esightcorp.mobile.app.home.navigation.HomeScreens.HomeFirstScreen.deviceArg

fun NavGraphBuilder.addHomeNavigation(navController: NavController) {
    navigation(startDestination = HomeScreens.HomePermissionScreen.route, route= HomeScreens.IncomingNavigationRoute.route){
        composable(
            route =HomeScreens.HomePermissionScreen.route){
            HomePermissionScreen(navController = navController)
        }
        composable(
            route = HomeScreens.HomeFirstScreen.routeWithArgs,
            arguments = arguments,
            deepLinks = listOf( navDeepLink {
                uriPattern = "android-app://androidx.navigation//${HomeScreens.HomeFirstScreen.route}/{${deviceArg}}"
            })
        ){ navBackStackEntry ->
            val device = navBackStackEntry.arguments?.getString(deviceArg)
            Log.d("TAG", "addHomeNavigation: $device")
            device?.let { HomeFirstScreen(navController = navController, it) }

        }

    }
}