package com.esightcorp.mobile.app.companion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.home.HomeScreen

@Composable
fun GoNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = GoScreens.HomeScreen.name
    ) {
        composable(GoScreens.HomeScreen.name){
            HomeScreen(navController = navController)
        }
    }
}