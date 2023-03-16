package com.esightcorp.mobile.app.btconnection.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class BtConnectionScreens(val route: String){
    object NoDevicesConnectedRoute: BtConnectionScreens("btconnection_home")
    object IncomingNavigationRoute: BtConnectionScreens("btconnection")
    object BtDevicesScreen: BtConnectionScreens("bt_devices")
    object BtDisabledScreen: BtConnectionScreens("bt_disabled")
    object BtSearchingRoute: BtConnectionScreens("bt_searching")
    object BTConnectingRoute: BtConnectionScreens("bt_connecting")
    object UnableToConnectRoute: BtConnectionScreens("unable_to_connect_bt")
    object NoDevicesFoundRoute: BtConnectionScreens("no_devices_found_bt")

    object BtConnectedRoute: BtConnectionScreens("bt_connected"){
        const val nameArg = "deviceName"
        const val addrArg = "deviceAddress"
        val routeWithArgs = "${route}/{$addrArg}/{$nameArg}"
        val arguments = listOf(
            navArgument(nameArg){type = NavType.StringType},
            navArgument(addrArg){type = NavType.StringType}
        )
    }

}
