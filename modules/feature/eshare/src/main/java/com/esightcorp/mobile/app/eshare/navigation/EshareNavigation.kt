/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.eshare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.esightcorp.mobile.app.eshare.composables.EshareConnectedRoute
import com.esightcorp.mobile.app.eshare.composables.EshareConnectionStoppedRoute
import com.esightcorp.mobile.app.eshare.composables.EshareSetupWifiRoute
import com.esightcorp.mobile.app.eshare.composables.EshareUnableToConnectRoute
import com.esightcorp.mobile.app.eshare.composables.EshareWifiDisabledRoute
import com.esightcorp.mobile.app.eshare.composables.HotspotSetupRoute
import com.esightcorp.mobile.app.ui.extensions.composable
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.ConnectedRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.ConnectionRejectedRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.ConnectionStoppedRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.HotspotSetupRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.UnableToConnectRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.WifiDisabledRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.WifiSetupRoute


fun NavGraphBuilder.addEshareNavigation(navController: NavController) {
    navigation(startDestination = ConnectedRoute.path, route = IncomingRoute.path) {
        composable(ConnectedRoute) { EshareConnectedRoute(navController) }

        composable(
            ConnectionStoppedRoute.routeWithArgs,
            arguments = ConnectionStoppedRoute.arguments,
        ) {
            EshareConnectionStoppedRoute(
                navController,
                reason = EShareStoppedReason.from(it.arguments?.getString(ConnectionStoppedRoute.reason))
            )
        }

        composable(UnableToConnectRoute) { EshareUnableToConnectRoute(navController) }

        composable(HotspotSetupRoute) { HotspotSetupRoute(navController) }

        composable(WifiDisabledRoute) { EshareWifiDisabledRoute(navController) }
        composable(ConnectionRejectedRoute) { EshareConnectedRoute(navController) }
        composable(WifiSetupRoute) { EshareSetupWifiRoute(navController) }
    }
}

enum class EShareStoppedReason {
    USER_DECLINED,
    EXISTING_ACTIVE_SESSION,
    REMOTE_STOPPED,
    HOTSPOT_ERROR,

    ;

    companion object {
        fun from(value: String?) = values().find { it.name == value }
    }
}
