package com.esightcorp.mobile.app.btconnection.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class BtConnectionScreens(val route: String){
    object BtConnectionHomeScreen: BtConnectionScreens("btconnection_home")
    object IncomingNavigationRoute: BtConnectionScreens("btconnection")
    object BtDevicesScreen: BtConnectionScreens("bt_devices")
    object BtDisabledScreen: BtConnectionScreens("bt_disabled")
    object BtSearchingRoute: BtConnectionScreens("bt_searching")
    object BtConnectedRoute: BtConnectionScreens("bt_connected"){
        const val nameArg = "deviceName"
        const val addrArg = "deviceAddress"
        val routeWithArgs = "${route}/{$addrArg}/{$nameArg}"
        val arguments = listOf(
            navArgument(nameArg){type = NavType.StringType},
            navArgument(addrArg){type = NavType.StringType}
        )
    }
    object BTConnectingRoute: BtConnectionScreens("bt_connecting")

}
