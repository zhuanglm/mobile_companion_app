package com.esightcorp.mobile.app.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.settings.DeviceDisconnectRoute
import com.esightcorp.mobile.app.settings.SettingsScreen
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation.*

fun NavGraphBuilder.addSettingsNavigation(navController: NavController) {
    navigation(startDestination = EntranceRoute.path, route = IncomingRoute.path) {
        composable(EntranceRoute.path) { SettingsScreen(navController) }

        composable(DisconnectRoute.path) { DeviceDisconnectRoute(navController) }
    }
}
