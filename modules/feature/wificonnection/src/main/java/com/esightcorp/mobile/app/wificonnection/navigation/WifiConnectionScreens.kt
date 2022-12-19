package com.esightcorp.mobile.app.wificonnection

sealed class WifiConnectionScreens(val route: String){
    object WifiConnectionHomeScreen: WifiConnectionScreens("wificonnection_home")
    object IncomingNavigationRoute: WifiConnectionScreens("wificonnection")
    object WifiCredentialsScreen: WifiConnectionScreens("wifi_credentials")
}
