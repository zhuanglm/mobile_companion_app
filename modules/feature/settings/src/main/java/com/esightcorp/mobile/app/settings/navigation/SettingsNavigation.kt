package com.esightcorp.mobile.app.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.settings.SettingsScreen

fun NavGraphBuilder.addSettingsNavigation(navController: NavController) {
    navigation(
        startDestination = SettingsScreens.SettingsEntranceRoute.route,
        route = SettingsScreens.IncomingNavigatorRoute.route,
    ) {
        composable(SettingsScreens.SettingsEntranceRoute.route) { SettingsScreen(navController) }
    }
}
