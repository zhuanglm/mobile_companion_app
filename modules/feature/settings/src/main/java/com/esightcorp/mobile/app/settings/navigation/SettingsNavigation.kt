/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.settings.DisconnectConfirmationRoute
import com.esightcorp.mobile.app.settings.SettingsScreen
import com.esightcorp.mobile.app.ui.navigation.SettingsNavigation.*

fun NavGraphBuilder.addSettingsNavigation(navController: NavController) {
    navigation(startDestination = EntranceRoute.path, route = IncomingRoute.path) {
        composable(EntranceRoute.path) { SettingsScreen(navController) }

        composable(DisconnectConfirmationRoute.path) { DisconnectConfirmationRoute(navController) }
    }
}
