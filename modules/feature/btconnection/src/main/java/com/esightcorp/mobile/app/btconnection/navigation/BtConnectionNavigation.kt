package com.esightcorp.mobile.app.btconnection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.esightcorp.mobile.app.btconnection.BtConnectedRoute
import com.esightcorp.mobile.app.btconnection.BtConnectingRoute
import com.esightcorp.mobile.app.btconnection.BtConnectionLostRoute
import com.esightcorp.mobile.app.btconnection.BtDevicesRoute
import com.esightcorp.mobile.app.btconnection.BtDisabledScreen
import com.esightcorp.mobile.app.btconnection.BtSearchingRoute
import com.esightcorp.mobile.app.btconnection.NoDeviceConnectedRoute
import com.esightcorp.mobile.app.btconnection.NoDevicesFoundRoute
import com.esightcorp.mobile.app.btconnection.UnableToConnectRoute
import com.esightcorp.mobile.app.ui.extensions.composable
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.BtConnectedRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.BtConnectingRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.BtDisabledScreen
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.BtSearchingRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.DeviceDisconnectedRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.IncomingRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.NoDeviceConnectedRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.NoDevicesFoundRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.ScanResultRoute
import com.esightcorp.mobile.app.ui.navigation.BtConnectionNavigation.UnableToConnectRoute


fun NavGraphBuilder.addBtConnectionNavigation(navController: NavController) {
    navigation(startDestination = NoDeviceConnectedRoute.path, route = IncomingRoute.path) {

        composable(NoDeviceConnectedRoute) { NoDeviceConnectedRoute(navController) }

        composable(BtSearchingRoute) { BtSearchingRoute(navController) }
        composable(ScanResultRoute) { BtDevicesRoute(navController) }
        composable(NoDevicesFoundRoute) { NoDevicesFoundRoute(navController) }
        composable(BtConnectingRoute) { BtConnectingRoute(navController) }
        composable(BtConnectedRoute) { BtConnectedRoute(navController) }
        composable(DeviceDisconnectedRoute) { BtConnectionLostRoute(navController) }

        composable(BtDisabledScreen) { BtDisabledScreen(navController) }
        composable(UnableToConnectRoute) { UnableToConnectRoute(navController) }
    }
}
