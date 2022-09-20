package com.esightcorp.mobile.app.home.navigation

sealed class WifiConnectionScreens(val route: String){
    object WifiConnectionHomeScreen: WifiConnectionScreens("wificonnection_home")
    object IncomingNavigationRoute: WifiConnectionScreens("wificonnection")
}
