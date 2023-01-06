package com.esightcorp.mobile.app.wificonnection

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class WifiConnectionScreens(val route: String){
    object WifiConnectionHomeScreen: WifiConnectionScreens("wificonnection_home")
    object IncomingNavigationRoute: WifiConnectionScreens("wificonnection")
    object WifiCredentialsScreen: WifiConnectionScreens("wifi_credentials"){
        const val ssidArg = "ssid"
        val routeWithArgs = "${route}/{${ssidArg}}"
        val arguments = listOf(
            navArgument(ssidArg){type = NavType.StringType}
        )
    }

}
