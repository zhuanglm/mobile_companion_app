package com.esightcorp.mobile.app.companion.navigation.toplevel

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.companion.navigation.SelectionScreen
import com.esightcorp.mobile.app.companion.navigation.go.addGoNavigation

@Composable
fun TopLevelNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SupportedProducts.SelectionScreen.route
    ) {
        composable(SupportedProducts.SelectionScreen.route){
            SelectionScreen(navController = navController)
        }
        //add all nested nav graphs below
        addGoNavigation(navController)
    }


}