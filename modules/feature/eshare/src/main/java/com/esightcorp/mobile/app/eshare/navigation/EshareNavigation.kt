package com.esightcorp.mobile.app.eshare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.esightcorp.mobile.app.eshare.composables.EshareBusyRoute
import com.esightcorp.mobile.app.eshare.composables.EshareConnectedRoute
import com.esightcorp.mobile.app.eshare.composables.EshareConnectionStoppedRoute
import com.esightcorp.mobile.app.eshare.composables.EshareUnableToConnectRoute
import com.esightcorp.mobile.app.eshare.composables.EshareWifiDisabledRoute
import com.esightcorp.mobile.app.eshare.composables.HotspotSetupRoute
import com.esightcorp.mobile.app.ui.extensions.composable
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.ConnectedRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.ConnectionStoppedRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.HotspotSetupRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.RemoteBusyRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.UnableToConnectRoute
import com.esightcorp.mobile.app.ui.navigation.EShareNavigation.WifiDisabledRoute


fun NavGraphBuilder.addEshareNavigation(navController: NavController) {
    navigation(startDestination = ConnectedRoute.path, route = IncomingRoute.path) {
        composable(ConnectedRoute) { EshareConnectedRoute(navController) }

        composable(ConnectionStoppedRoute) { EshareConnectionStoppedRoute(navController) }

        composable(UnableToConnectRoute) { EshareUnableToConnectRoute(navController) }

        composable(HotspotSetupRoute) { HotspotSetupRoute(navController) }

        composable(RemoteBusyRoute) { EshareBusyRoute(navController) }

        composable(WifiDisabledRoute) { EshareWifiDisabledRoute(navController) }
    }
}
