package com.esightcorp.mobile.app.btconnection.navigation

sealed class BtConnectionScreens(val route: String){
    object BtConnectionHomeScreen: BtConnectionScreens("btconnection_home")
    object IncomingNavigationRoute: BtConnectionScreens("btconnection")
}
