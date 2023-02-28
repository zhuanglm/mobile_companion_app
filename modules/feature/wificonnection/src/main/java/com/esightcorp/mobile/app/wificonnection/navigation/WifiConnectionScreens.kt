package com.esightcorp.mobile.app.wificonnection

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class WifiConnectionScreens(val route: String){
    object WifiConnectionHomeScreen: WifiConnectionScreens("wificonnection_home")
    object IncomingNavigationRoute: WifiConnectionScreens("wificonnection")
    object WifiCredentialsScreen: WifiConnectionScreens("wifi_credentials")/*{
        const val ssidArg = "ssid"
        val routeWithArgs = "${route}/{${ssidArg}}"
        val arguments = listOf(
            navArgument(ssidArg){type = NavType.StringType}
        )
    }*/
    object SearchingForNetworkRoute: WifiConnectionScreens("searching_for_networks")
    object SelectNetworkRoute: WifiConnectionScreens("select_network")
    object EnterPasswordRoute: WifiConnectionScreens("enter_password")
    object WifiQRCodeRoute: WifiConnectionScreens("wifi_qrcode")
    object HowToScanQrRoute: WifiConnectionScreens("help_wifi_qrcode")
    object AdvancedNetworkSettingsRoute: WifiConnectionScreens("advanced_network_settings")
    object SelectNetworkSecurityRoute: WifiConnectionScreens("select_network_security")
    object UnableToConnectRoute: WifiConnectionScreens("unable_to_connect")

}
