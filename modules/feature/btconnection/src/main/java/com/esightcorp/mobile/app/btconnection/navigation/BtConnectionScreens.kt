package com.esightcorp.mobile.app.btconnection.navigation

sealed class BtConnectionScreens(val route: String){
    object BtConnectionHomeScreen: BtConnectionScreens("btconnection_home")
    object IncomingNavigationRoute: BtConnectionScreens("btconnection")
    object BtDevicesScreen: BtConnectionScreens("bt_devices")
    object BtDisabledScreen: BtConnectionScreens("bt_disabled")
    object BtSearchingRoute: BtConnectionScreens("bt_searching")

}
